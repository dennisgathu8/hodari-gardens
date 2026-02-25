FROM clojure:temurin-17-tools-deps AS builder

# Install Node.js for Shadow-CLJS
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs

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

# Copy static assets
COPY --from=builder /app/resources/public ./resources/public

EXPOSE 3000

ENV PORT=3000

CMD ["java", "-jar", "hodari-gardens.jar"]
