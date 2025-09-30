// MongoDB initialization script to create application user
db = db.getSiblingDB('weather_alert_db');

// Create application user with read/write permissions
db.createUser({
  user: 'weatheruser',
  pwd: 'weatherpass',
  roles: [
    {
      role: 'readWrite',
      db: 'weather_alert_db'
    }
  ]
});

// Create collections with initial indexes
db.createCollection('users');
db.createCollection('weather_alerts');
db.createCollection('alert_history');

// Create indexes for better performance
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "phoneNumber": 1 });
db.weather_alerts.createIndex({ "userId": 1 });
db.weather_alerts.createIndex({ "location": 1 });
db.alert_history.createIndex({ "userId": 1 });
db.alert_history.createIndex({ "createdAt": 1 });

print('MongoDB initialization completed successfully');
