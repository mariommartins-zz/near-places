# Near Places
![](https://github.com/mario-martins-ifood/near-places/blob/master/documentation/feature_demo.gif)

## Libraries
This project was made in Kotlin unsing AndroidX and uses the following libs (the major libs are in bold):
- **Koin for dependency injection**
- **Coroutines for multithreading**
- **Retrofit for API requests**
- **Navigation**
- **DataBinding**
- **Paging**
- Moshi as Json parser
- Timber
- Glide
- SwipeRefreshLayout
- ConstraintLayout
- Lottie
- Cardview
- Ktx
- Play Services
- **Espresso for instrumented testing**
- **Mockito for unit testing**

## Functionality
This is a single-activity app that uses the device location, fetch the nearby places using Google Places API and display a paged list of results.
Users will be able to select between 3 categories -> Cafes, Bars and Restaurants.
The app shows the ratings of the place and a icon that states if it's open or not.
The app has a cache system in order to avoid multiple calls to the server for the same location/page.

## Endpoint documentation

*URL:*
GET https://maps.googleapis.com/maps/api/place/nearbysearch/json?

*Query parameters:*
- Required: `location` - a space-separated string of search terms
- Required: `radius` - the radius of the search in meters
- Required: `key` - the Google API Key (it's in the repository but it's safe because I added restrictions)
- Optional: `type` - a string stating what type of places is going to be requested
- Optional: `pagetoken` - a string that identify page to be requested for the same location
(Example: each page has 20 results)

*Response:*
The `results` object is an array of place results.
- Find id of a place at `results[].place_id`
- Find name of a place at `results[].name`
- Find icon of a place at `results[].icon`
- Find the open status of a place at `results[].openingHours?.openNow`
- Find rating of a place at `results[].rating`

## Notes
- This was built to be maintained for a while.
- The architecture used was MVVM, with DataSources & Repositories.
- The code design and style was intended to be consistent and reasonable
- Please add your own Google Api Key
- The instrumented tests stopped working for some lib related issue and is going to be fixed in next implementations

Please ask if you have any questions!
