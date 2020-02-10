# Mixr
Android music streaming App that allows users to search through SoundClouds database of songs and select a track for media playback.

Typing away during development this experience just confirmed my love for android, really nailing in my committed future to the app dev space!
Some of the features used here were enabled by a wide range of modern Android technologies such as,

- Working with the newer RecyclerView to display a list of tracks
- Implementing an ActionBar to be used in the main menu for search functionality
- Using Parcelable to pass a track object from the list activity to a MediaPlayer activity for playback
- Setting up the fantastic combination of RetroFit and GSON, to handle networking
- Structuring UI utilizing ConstraintLayout and RelativeLayout using XML
- Placing androidx.widget components for music player related functionality

# Dependencies
* [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) A scrollable list populated by Track Objects from SoundCloud
* [ActionBar](https://developer.android.com/reference/android/app/ActionBar) A bar at the top of the main list activity for Searching Tracks on SoundCloud
* [Parcellable](https://developer.android.com/reference/android/os/Parcelable) Allows passing of a Track Object between activities for quick retrieval of track information
* [Gson](https://github.com/google/gson) Eases creation of Java Objects from JSON Objects
* [GsonConverter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) Used by RetroFit to parse HTTP responses into an object
* [RetroFit](https://github.com/square/retrofit) REST client allowing HTTP calls to Web APIs in this case SoundClouds API
* [Picasso](https://github.com/square/picasso) Image downloader and caching library for quickly grabbing Album images off SoundCloud

# Pictures
[![Image from Gyazo](https://i.gyazo.com/8bf4d9c459ee8261e8d7455b3ac08264.gif)](https://gyazo.com/8bf4d9c459ee8261e8d7455b3ac08264)
[![Image from Gyazo](https://i.gyazo.com/4c5c3724b998778bdd8f72e9c6ee1943.gif)](https://gyazo.com/4c5c3724b998778bdd8f72e9c6ee1943)
[![Image from Gyazo](https://i.gyazo.com/f94ba7b262d9aaba9ea450ad36595445.gif)](https://gyazo.com/f94ba7b262d9aaba9ea450ad36595445)
