#!/bin/bash

# Weather Alert System Build and Deploy Script

echo "🌦️ Building Weather Alert System..."

# Clean and build the application
echo "📦 Building application..."
./gradlew clean build

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful!"

# Run tests
echo "🧪 Running tests..."
./gradlew test

if [ $? -ne 0 ]; then
    echo "❌ Tests failed!"
    exit 1
fi

echo "✅ All tests passed!"

# Build Docker image
echo "🐳 Building Docker image..."
docker build -t weather-alert-system .

if [ $? -ne 0 ]; then
    echo "❌ Docker build failed!"
    exit 1
fi

echo "✅ Docker image built successfully!"

# Start services with Docker Compose
echo "🚀 Starting services..."
docker-compose up -d

if [ $? -ne 0 ]; then
    echo "❌ Failed to start services!"
    exit 1
fi

echo "✅ Weather Alert System is running!"
echo "📊 API available at: http://localhost:8080/api"
echo "🔍 Health check: http://localhost:8080/actuator/health"
echo "📈 Metrics: http://localhost:8080/actuator/metrics"
