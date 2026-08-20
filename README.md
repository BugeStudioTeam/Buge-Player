# Buge Player

<p align="center">
  <img src="https://raw.githubusercontent.com/BugeStudioTeam/Buge-Player/main/images/icons/icon.png" alt="Buge Player Icon" width="120"/>
</p>

<p align="center">
  <img src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Android/android2.svg">
  <img src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Kotlin/kotlin2.svg">
  <img src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Github/github3.svg">
  <img src="https://ziadoua.github.io/m3-Markdown-Badges/badges/LicenceGPLv3/licencegplv32.svg">
</p>

**Buge Player** is a polished, privacy-first Android music and video player built with **Kotlin**, **Media3**, and **Material Design 3 Expressive**. It combines audio and video playback, a refined modern interface, dynamic artwork colors, multilingual support, and a standalone floating player for media opened from other applications.

---

## 📸 Screenshots

<div style="overflow-x: auto; white-space: nowrap; padding: 10px 0;">
NONE NOW
</div>

---

## ✨ Features

### 🚀 Core Capabilities

| Feature                             | Description                                                                      |
| ----------------------------------- | -------------------------------------------------------------------------------- |
| **Media3 Playback**                 | Powered by AndroidX Media3 for robust audio and video playback                   |
| **Local & Network Media**           | Play files from device storage or stream HTTP/HTTPS and HLS/M3U8 content         |
| **Material Design 3 Expressive UI** | Clean, system-themed interface with dynamic colors and Google Sans Flex typography |
| **Privacy-First**                   | No ads, no analytics, no cloud backend — your data stays on your device          |

---

### 🎵 Music Player

- Full **Now Playing** experience with album artwork
- **Queue management** — add, reorder, clear, and persist your queue
- Playback modes: **Shuffle**, **Repeat Off**, **Repeat One**, **Repeat All**
- **Speed control** from 0.5x to 2.0x
- **Favourites** and **recent media** (last 10 items)
- Six random media recommendations below the active playback controls
- Background playback with system notification and lock screen controls

---

### 🎬 Video Player

- Embedded video playback with immersive **full-screen mode**
- Tap screen to reveal **centered play/pause** and an elevated seek bar
- Full-screen controls **auto-hide after 3 seconds** of inactivity
- System back exits full screen before returning to the previous view
- First-frame extraction for library artwork and dynamic color generation

---

### 📂 Open Media from Other Apps

- Register as a handler for supported audio/video MIME types via `ACTION_VIEW`
- Open media from file managers, galleries, or third-party apps with **Open with → Buge Player**
- Launches a standalone **floating player** with media title, video surface, progress controls, and immersive full-screen action
- Closing the floating player stops playback and clears the background service

---

### 🌐 Network Media Playback

- Built-in **Add network media** workflow
- Supports direct MP3, AAC, MP4, WebM, MKV, and HLS `.m3u8` streams
- Automatically detects HLS and lets users choose audio or video handling

---

### 🎨 Personalization

| Option                        | Details                                                                 |
| ----------------------------- | ----------------------------------------------------------------------- |
| **Dynamic Color**             | Android 12+ Material You theming with artwork/video-frame extraction    |
| **Theme Mode**                | Light, Dark, and System-following                                       |
| **Accent Colors**             | Selectable preset accent colors                                         |
| **Typography**                | Google Sans Flex variable font for expressive display and metadata      |
| **Keep Screen On**            | Optional screen keep-alive during active playback                       |

---

### 📱 Interface Highlights

- First-launch welcome flow with guided setup
- Navigation transitions and rounded selection states
- Animated mini-player with progress ring for at-a-glance playback status
- Favourites appear first in the library, followed by recent media

---

## 📋 Requirements

| Requirement | Minimum Version          |
| ----------- | ------------------------ |
| Android OS  | 8.0 (API 26) or higher   |

> **Note**: Buge Player does not require any special system privileges. Network playback requires an active internet connection.

---

## 📦 Installation

[<img src="https://raw.githubusercontent.com/BugeStudioTeam/Buge-App-Manager/refs/heads/main/images/README/Release/get-it-on-github.svg"
    alt="Get it on GitHub"
    height="80">](https://github.com/BugeStudioTeam/Buge-Player/releases)[<img src="https://raw.githubusercontent.com/BugeStudioTeam/Buge-App-Manager/refs/heads/main/images/README/Release/get-it-on-telegram.svg"
 alt="Get it on Telegram"
 height="80">](https://t.me/bugestudio)

---

## 🌐 Supported Languages

- English - en
- Français - fr
- Deutsch - de
- Русский - ru
- Português - pt
- Português (Brasil) - pt-rBR
- Español - es
- 中文 (简体) - zh
- 中文 (繁体) - zh-rTW
- العربية - ar
- 日本語 - ja
- 한국어 - ko

> [If you'd like to request support for a specific language, please open an issue and let us know](https://github.com/BugeStudioTeam/Buge-Player/issues)

---

## 🔒 Privacy

| Aspect                              | Details                                                                 |
| ----------------------------------- | ----------------------------------------------------------------------- |
| **No Tracking**                     | No advertising SDKs, analytics, account sign-in, or cloud backend       |
| **Local Storage**                   | Favourites, queue, language, theme, and recent media are stored locally |
| **Media Permission**                | Requested only for explicit library scanning                            |
| **External Media Sharing**          | Read via URI permission granted by the source app                       |
| **Shared Media Caching**            | Cached copies stored in app-scoped cache with temporary read permission |

---

## ⭐ Star History

<a href="https://www.star-history.com/?repos=BugeStudioTeam%2FBuge-Player&type=date&legend=top-left">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/chart?repos=BugeStudioTeam/Buge-Player&type=date&theme=dark&legend=top-left" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/chart?repos=BugeStudioTeam/Buge-Player&type=date&legend=top-left" />
   <img alt="Star History Chart" src="https://api.star-history.com/chart?repos=BugeStudioTeam/Buge-Player&type=date&legend=top-left" />
 </picture>
</a>

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0**.
