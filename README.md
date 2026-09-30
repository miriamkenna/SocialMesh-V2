# SocialMesh

**SocialMesh** is an innovative mobile platform designed to foster new connections between people attending the same events, turning every occasion into a social opportunity.

Through an intuitive interface, users can see who is attending a specific event, explore people’s locations on the map, apply search filters, and interact through a like and match system.

Thanks to features such as **Map Mode**, the **attendee list**, and **custom filters**, SocialMesh offers a dynamic experience, ideal both for those who want to make new friends and for those simply looking for company at an event.

---

## Table of Contents

* [Description](#description)
* [Requirements](#requirements)
* [Installation and Configuration](#installation-and-configuration)
* [Application Usage](#application-usage)
* [Repository Structure](#repository-structure)
* [Contributors](#contributors)
* [License](#license)
* [Contacts and Support](#contacts-and-support)

---

## Description

SocialMesh is an Android app designed to enhance the social experience connected to events.
With SocialMesh, you can:

* **Match** with other event attendees
* Use **Map Mode** to explore who is around you
* View the **attendee list** for an event
* Send **likes** and receive matches
* Apply **filters** to find like-minded people

The app is powered by the Ticketmaster API for event retrieval and uses Google Maps for interactive visualization.

> **Note:** The Ticketmaster API only works in the United States, so it is recommended to set the emulator’s mock location to **Indianapolis** — an area rich in events.

---

## Requirements

* Language: Java
* Build System: Gradle
* Recommended IDE: Android Studio
* Android emulator with mock location enabled, recommended: Indianapolis, USA

---

## Installation and Configuration

### 1. Clone the repository

```bash
git clone https://github.com/MartinaKenna/SocialMesh.git
```

### 2. Configure the API Keys

Create or edit the `local.properties` file in the project root with:

```properties
events_api_key=<apiKey>
MAPS_API_KEY=<apiKey>
```

### 3. Set the location on the Android emulator

* Open the emulator
* Set a mock location to **Indianapolis, USA**

### 4. Launch the app

```bash
./gradlew build
./gradlew installDebug
```

---

## Application Usage

* Allow location permissions
* Navigate the interactive map to view events and people
* Open an event to see who is attending
* Explore profiles, send likes, and match
* Apply filters to find people with similar interests

---

## Repository Structure

```text
app/                  → Source code
Screenshot/           → Interface images
Documentazione/       → Resources and explanations
build.gradle.kts      → Build system configuration
local.properties      → Local file containing API keys, not included in the repo
.gitignore            → Files excluded from version control
```

---

## Contributors

* Martina Kenna – 879403
* Giovanni Mensi – 886516
* Francesco Barresi – 905027

---

## License

License not specified.
Please contact the developers for usage or collaboration.

---

## Contacts and Support

For questions, issues, or proposals:

* Open an [issue](https://github.com/MartinaKenna/SocialMesh/issues)
* Or contact the team directly

---

With **SocialMesh**, every event becomes an opportunity to meet someone special.
**Match. Connect. Live the event.**
