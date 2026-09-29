# File Sharing Client-Server Application 📁🌐

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.java.com)
[![Swing](https://img.shields.io/badge/Swing-UI-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Networking](https://img.shields.io/badge/Networking-TCP%20%26%20UDP-green.svg)]()
[![Gson](https://img.shields.io/badge/Gson-JSON-red.svg)](https://github.com/google/gson)
[![MD5](https://img.shields.io/badge/Hashing-MD5-lightgrey.svg)]()

> A Java-based client-server application for **secure user authentication** and **peer-to-peer file sharing** between users. The system uses **TCP for reliable control messages** (login, requests, commands) and **UDP for high-throughput file transfer**, with progress tracking and JSON-based persistent storage on the server.

## 📖 Overview

This project implements a file sharing system where multiple clients connect to a central server. Users can:
- Register and log in with a username and password (hashed with MD5).
- Upload files to the server.
- Browse a list of their own files.
- Send file requests to other users.
- Download files that have been shared with them (after request approval).
- Track upload/download progress via a progress bar.

The server maintains a live list of connected clients, saves user data (credentials, files, requests) to JSON files using Gson, and handles concurrent clients using a thread-per-connection model.

## ✨ Features

### 🔐 User Authentication
- Sign up with unique username and password.
- Passwords are hashed using **MD5** before storage.
- Log in validation with clear error messages.
- Online/Offline status tracking for each user.

### 📂 File Management
- Upload files from the client to the server.
- Files are stored per-user in `src/main/java/SavedFiles/<username>/`.
- Automatic handling of duplicate filenames (e.g., `file(1).txt`).
- List all files belonging to the logged-in user.

### 📨 Request System
- Send a file request to another user by username and filename.
- The target user receives the request and can **Accept** or **Reject** it.
- Only accepted requests allow the requester to download the file.
- Server maintains both pending and accepted requests per user.

### 📊 Progress Tracking
- Upload and download operations show a progress bar in the client GUI.
- Progress is calculated based on the number of packets sent/received.

### 🖥️ Server GUI
- A Swing-based server window displays the list of connected clients.
- Shows each user's **online/offline** status in real time.
- New connections are announced via a popup dialog.

### 💾 Persistent Storage
- All user data (username, hashed password, files, requests) is saved to JSON.
- Uses **Gson** with pretty printing for readable config files.
- Data is reloaded when the server restarts.

## 🏗️ Architecture

The system is built on a **hybrid TCP/UDP model**:

| Channel | Protocol | Purpose |
|---------|----------|---------|
| **Control** | TCP | Login, signup, commands, requests, file listing |
| **Data** | UDP | File upload/download (bulk data transfer) |

### Server Side
- `ServerMain` → entry point, launches `ServerApplication`.
- `TCPServer` → accepts client connections, spawns a `ServerListener` per client.
- `RequestHandler` → processes control messages (login, upload, requests, etc.).
- `UDPServer` → handles UDP packets for file transfer.
- `Receiver` / `Sender` → receive and send file chunks over UDP.
- `FileHandler` → reads files into packets, writes received packets to disk.
- `Configs` → saves/loads user data as JSON (via Gson).
- `Hashing` → MD5 hashing utility.
- `ServerFrame` / `UserPanel` → Swing GUI for the server.

### Client Side
- `ClientMain` → entry point, launches `ClientApplication`.
- `TCPClient` → sends control messages and receives server responses.
- `ClientListener` → wraps socket streams for reading/writing.
- `UDPClient` → manages UDP socket for file transfer.
- `Receiver` / `Sender` → same packet logic, but on the client side.
- `ReceiveProgress` / `SendProgress` → progress bar frames.
- `ClientFrame` / `MainMenu` / `SignUpLogIn` → Swing UI.


