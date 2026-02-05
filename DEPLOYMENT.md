# Hodari Gardens Resort - Deployment Guide

This guide provides step-by-step instructions for deploying the Hodari Gardens Resort application to production.

## Prerequisites

- Java JDK 11+ installed
- Clojure CLI tools installed
- Node.js 16+ and npm installed
- Docker (optional, for containerized deployment)
- Production server with minimum 2GB RAM

## Quick Deployment Steps

### 1. Prepare the Application

```bash
# Clone or download the project
cd hodari-gardens

# Install dependencies
npm install

# Build the frontend
npm run build

# This creates optimized JavaScript in resources/public/js/
```

### 2. Build Production Uberjar

```bash
# Create production uberjar
clj -T:build uber

# This creates target/hodari-gardens-1.0.0.jar
```

### 3. Deploy to Server

#### Option A: Direct Java Deployment

```bash
# Copy uberjar to server
scp target/hodari-gardens-1.0.0.jar user@server:/opt/hodari-gardens/

# SSH into server
ssh user@server

# Run the application
cd /opt/hodari-gardens
java -jar hodari-gardens-1.0.0.jar

# Or run as background service
nohup java -jar hodari-gardens-1.0.0.jar > app.log 2>&1 &
```

#### Option B: Docker Deployment

```bash
# Build Docker image
docker build -t hodari-gardens:latest .

# Run container
docker run -d \
  --name hodari-gardens \
  -p 3000:3000 \
  --restart unless-stopped \
  hodari-gardens:latest

# View logs
docker logs -f hodari-gardens
```

#### Option C: Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - PORT=3000
    restart: unless-stopped
    volumes:
      - ./logs:/app/logs
```

Deploy:

```bash
docker-compose up -d
```

## Environment Configuration

### Environment Variables

- `PORT` - Server port (default: 3000)

Set environment variables:

```bash
# Linux/Mac
export PORT=8080

# Windows
set PORT=8080

# Docker
docker run -e PORT=8080 hodari-gardens:latest
```

## Systemd Service (Linux)

Create `/etc/systemd/system/hodari-gardens.service`:

```ini
[Unit]
Description=Hodari Gardens Resort Application
After=network.target

[Service]
Type=simple
User=hodari
WorkingDirectory=/opt/hodari-gardens
ExecStart=/usr/bin/java -jar /opt/hodari-gardens/hodari-gardens-1.0.0.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=hodari-gardens

Environment="PORT=3000"

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl daemon-reload
sudo systemctl enable hodari-gardens
sudo systemctl start hodari-gardens
sudo systemctl status hodari-gardens
```

## Nginx Reverse Proxy

Create `/etc/nginx/sites-available/hodari-gardens`:

```nginx
server {
    listen 80;
    server_name hodarigardens.co.ke www.hodarigardens.co.ke;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Static assets caching
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        proxy_pass http://localhost:3000;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

Enable site:

```bash
sudo ln -s /etc/nginx/sites-available/hodari-gardens /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

## SSL/HTTPS with Let's Encrypt

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Obtain certificate
sudo certbot --nginx -d hodarigardens.co.ke -d www.hodarigardens.co.ke

# Auto-renewal is configured automatically
# Test renewal
sudo certbot renew --dry-run
```

## Performance Optimization

### JVM Options

For production, use optimized JVM settings:

```bash
java -Xms512m -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar hodari-gardens-1.0.0.jar
```

### Nginx Caching

Add to nginx config:

```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=hodari_cache:10m max_size=1g inactive=60m;

location / {
    proxy_cache hodari_cache;
    proxy_cache_valid 200 10m;
    proxy_cache_use_stale error timeout http_500 http_502 http_503 http_504;
    # ... other proxy settings
}
```

## Monitoring

### Application Logs

```bash
# Systemd logs
sudo journalctl -u hodari-gardens -f

# Docker logs
docker logs -f hodari-gardens

# File logs (if configured)
tail -f /opt/hodari-gardens/app.log
```

### Health Check Endpoint

Add to your monitoring system:

```bash
# Check if app is running
curl http://localhost:3000/api/rooms

# Expected: JSON response with rooms data
```

## Backup Strategy

### Database Backup (if using database)

```bash
# Backup EDN data files
tar -czf hodari-backup-$(date +%Y%m%d).tar.gz resources/data/
```

### Automated Backups

Create cron job:

```bash
# Edit crontab
crontab -e

# Add daily backup at 2 AM
0 2 * * * /opt/hodari-gardens/backup.sh
```

## Troubleshooting

### Application Won't Start

```bash
# Check Java version
java -version

# Check port availability
sudo lsof -i :3000

# Check logs
sudo journalctl -u hodari-gardens -n 100
```

### High Memory Usage

```bash
# Check Java process
ps aux | grep java

# Adjust JVM heap size
java -Xmx1g -jar hodari-gardens-1.0.0.jar
```

### Slow Response Times

1. Check server resources: `htop`
2. Review nginx logs: `tail -f /var/log/nginx/access.log`
3. Enable nginx caching (see above)
4. Consider CDN for static assets

## Security Checklist

- [ ] Change default ports if needed
- [ ] Enable HTTPS with valid SSL certificate
- [ ] Configure firewall (ufw/iptables)
- [ ] Set up fail2ban for SSH protection
- [ ] Regular security updates: `sudo apt update && sudo apt upgrade`
- [ ] Restrict file permissions: `chmod 600 hodari-gardens-1.0.0.jar`
- [ ] Use environment variables for sensitive data
- [ ] Enable nginx rate limiting
- [ ] Regular backups
- [ ] Monitor logs for suspicious activity

## Scaling

### Horizontal Scaling

Use a load balancer (nginx, HAProxy) with multiple instances:

```nginx
upstream hodari_backend {
    server 127.0.0.1:3000;
    server 127.0.0.1:3001;
    server 127.0.0.1:3002;
}

server {
    location / {
        proxy_pass http://hodari_backend;
    }
}
```

### Vertical Scaling

Increase server resources and adjust JVM settings:

```bash
java -Xms2g -Xmx4g -jar hodari-gardens-1.0.0.jar
```

## Rollback Procedure

```bash
# Keep previous version
mv hodari-gardens-1.0.0.jar hodari-gardens-1.0.0.jar.backup

# If issues occur, rollback
sudo systemctl stop hodari-gardens
mv hodari-gardens-1.0.0.jar.backup hodari-gardens-1.0.0.jar
sudo systemctl start hodari-gardens
```

## Support

For deployment issues:
- Email: dev@hodarigardens.co.ke
- Check logs first
- Review this guide
- Consult README.md for development setup

---

**Last Updated**: February 2026
