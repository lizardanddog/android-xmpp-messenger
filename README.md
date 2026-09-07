# Android XMPP Messenger

Native Android messaging prototype built in Java, featuring real-time XMPP communication, multimedia messaging, local persistence and custom Android UI components.

> Originally developed in 2022 and later published as part of our development portfolio.

## Overview

This project is an experimental native Android messaging application developed to explore real-time communication and multimedia features without relying on a cross-platform framework.

The application combines XMPP messaging with local data persistence, image and video sharing, voice messages, Camera2 integration and custom-drawn Android views.

It was one of our early Android/Java projects and is preserved here as a technical portfolio project.

## Features

- Real-time messaging using XMPP
- XMPP connection management using Smack
- Android background service for messaging
- Local conversation and message persistence
- Image messaging
- Video messaging
- Voice message recording and playback
- Media upload and download over HTTP
- Camera2 photo capture
- Camera2 video recording
- Front/back camera switching
- Flash controls
- Video thumbnail generation
- Audio waveform visualization
- Custom chat interface
- Custom Android Views
- Canvas-based UI components
- Gesture and touch handling
- Custom animations

## Tech Stack

- Java
- Android SDK
- XMPP
- Smack 4.4
- SQLite
- Volley
- Camera2 API
- MediaRecorder
- MediaPlayer
- Android Visualizer API
- Canvas / Paint
- Gradle

## Architecture

The project combines several Android components:

```text
                    Android Application
                           |
          +----------------+----------------+
          |                                 |
      Chat UI                         Camera / Media
          |                                 |
   Custom Views                       Camera2 API
   Canvas / Paint                     MediaRecorder
          |                           MediaPlayer
          |
    ChatBoxActivity
          |
    +-----+-------+
    |             |
  XMPP          SQLite
    |             |
 Smack       Local messages
    |
XMPP Service
    |
Remote messaging server


Media messages
      |
    Volley
      |
HTTP media backend
