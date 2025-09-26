# Ocean Notes - Kotlin Android App (Traditional Views)

A modern, minimalist notes application implementing CRUD operations with a clean "Ocean Professional" theme (blue & amber accents), built using traditional Android Views, Material Components, Room (local storage), and ViewBinding. No Jetpack Compose is used.

Features:
- Create, view, edit, and delete notes
- Searchable notes list
- Smooth, user-friendly interactions
- Subtle gradients, rounded corners, shadows

Build:
- Ensure JDK 17 is available and ANDROID_HOME SDK path is configured.
- From the project root:
  ./gradlew :app:assembleDebug

Install:
- Connect a device or start an emulator, then:
  ./gradlew :app:installDebug

Run:
- Launch "Ocean Notes" app on your device.

Project notes:
- UI uses XML layouts with ViewBinding.
- Local persistence via Room (ocean_notes.db).
- Entry point: com.oceanpro.notes.ui.NotesListActivity
