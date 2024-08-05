# Energy Management System Project

## Overview
This project is an Energy Management System built using a microservices architecture. The system includes several components such as user management, device management, monitoring, and communication features. It uses Docker for deployment and includes a front-end developed with React.js.

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Microservices](#microservices)
    - [User Microservice](#user-microservice)
    - [Device Microservice](#device-microservice)
    - [Device Simulator](#device-simulator)
    - [Monitoring Microservice](#monitoring-microservice)
    - [Chat Microservice](#chat-microservice)
    - [Authentication Microservice](#authentication-microservice)
    - [Frontend](#frontend)
3. [Databases](#databases)
4. [Docker Deployment](#docker-deployment)

## System Architecture
The system is designed using a microservices architecture, which structures the application as a collection of small, independent, and loosely coupled services. Each microservice is responsible for a specific business capability and can be developed, deployed, and scaled independently.

## Microservices

### User Microservice
- Back-end (Spring Boot) that handles CRUD operations on users.
- Two types of users: admin and client.
- Admins can perform any operation, while clients can only view their devices.

### Device Microservice
- Back-end (Spring Boot) that manages operations on devices.
- Synchronizes user data with the User Microservice.
- Ensures devices are assigned to existing users and cleans up when users are deleted.

### Device Simulator
- A desktop application that simulates device readings.
- Configurable via a file and command line arguments.
- Reads measurement values from `sensor.csv`, generates timestamps, and sends JSON strings to `measurements_queue` every 10 minutes.

### Monitoring Microservice
- Back-end (Spring Boot) that monitors device measurements received through RabbitMQ.
- Saves measurements to the database and computes hourly consumption.
- Sends notifications via websockets if consumption exceeds limits.
- Synchronizes with the Device Microservice for new devices.

### Chat Microservice
- Back-end (Spring Boot) which enables real-time, bidirectional communication between users and the administrator using WebSockets.
- Implements message topics to differentiate between client and administrator messages.
- Supports various messaging features including text messages, typing indicators, stopped typing indicators, and read receipts.

### Authentication Microservice
- Back-end (Spring Boot) that provides secure access to the system’s microservices.
- Implements login functionality with Spring Security and JWT Tokens.
- Passwords are hashed before storage.
- Issues tokens for accessing other endpoints upon successful authentication.

### Frontend
- Developed using React.js.
- Composed of modular and reusable components.
- Communicates with backend services via REST APIs.
###

## Databases
The system utilizes PostgreSQL databases to store information for various microservices. Each microservice has its own dedicated database to ensure data isolation and optimized performance.

### User Database
- Stores user information including credentials and roles.

### Device Database
- Stores information about devices including their associated users.

### Monitoring Database
- Stores device measurements and hourly consumption data for monitoring and analysis.

### Chat Database
- Stores chat messages between users and the administrator, including metadata for message states.

# Docker Deployment

The application deploys on Docker, organized into multiple containers:

- **PostgreSQL Servers Containers**: For user, device, monitoring, and chat databases.
- **Service Containers**: For Spring Boot microservices (user, device, monitoring, authentication, and chat microservices).
- **Frontend Container**: For running the React application.

All services are connected through a custom Docker network to facilitate inter-service communication.

### Deployment Instructions

The whole application can be deployed by running `docker-compose up -d` from the project root.

