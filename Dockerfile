FROM node:20-alpine AS node-provider

FROM clojure:temurin-17-tools-deps AS builder
COPY --from=node-provider /usr/local/bin/node /usr/local/bin/node
COPY --from=node-provider /usr/local/lib/node_modules /usr/local/lib/node_modules
COPY --from=node-provider /usr/local/bin/npm /usr/local/bin/npm

WORKDIR /app

# Copy dependency files
COPY deps.edn package.json package-lock.json* ./
COPY shadow-cljs.edn tailwind.config.js build.clj ./

# Install dependencies
RUN npm install
RUN clojure -P -M:dev

# Copy source code
COPY src/ ./src/
COPY resources/ ./resources/

# Build frontend
RUN npm run build

# Build backend uberjar with memory limits to prevent Docker OOM kills
ENV _JAVA_OPTIONS="-Xmx2g"
RUN clojure -T:build uber

# Production image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy uberjar from builder
COPY --from=builder /app/target/hodari-gardens-1.0.0.jar ./hodari-gardens.jar

# Copy static assets and data files
COPY --from=builder /app/resources ./resources

EXPOSE 3000

ENV PORT=3000

CMD ["java", \
     "-Xms128m", \
     "-Xmx700m", \
     "-XX:+UseG1GC", \
     "-XX:+ExitOnOutOfMemoryError", \
     "-Dlog4j2.formatMsgNoLookups=true", \
     "-jar", "hodari-gardens.jar"]
