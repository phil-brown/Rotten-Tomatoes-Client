Android Code Challenge
Summary

Create an application that uses the Rotten Tomatoes API (http://developer.rottentomatoes.com/docs/read/Home) display data from Rotten Tomatoes. The main page of the application should show the top 10 Box Office Earnings movies in a List Activity. Each cell should contain the title of the movie, the MPAA rating, the image of the poster and the critics score (viewed in a ProgressBar). The cell's background should alternate between white and #F2F2F2. The title should be bold system font of size 24 with a minimum size of 14. The rating should use the appropriate rating image and be positioned 5 points away from the end of the title.  A screenshot of what this view should look like is in the assets files.
Tapping on a row should take you to a detail page about the movie where all the information is shown from the cell and the synopsis, runtime in formatted in hours and minutes, and abridged cast is shown. The entire view should be contained within a single scroll view. Tapping the menu button should allow the user to tweet about the movie and contain a link to the full movie page. The layout should have (ordered from top to bottom)
the movie poster centered in the page
a bold label with text of "Synopsis"
the synopsis
A bold label with text of Cast
The abridged cast as [ACTOR as ROLE]
A thin black divider
Then on a single line have label with text "Rated [XYZ] • Freshness: [XY] • Runtime: [X] hr [YZ] min"
A screenshot of what this view should look like is in the assets files.
The assets for the application to use are at http://dl.dropbox.com/u/2141923/AndroidCodeChallengeAssets.zip. Note, some of the assets provided may not be useful for your application.

You must do one of the following tasks as well (only do one):
Localize the application: Make the application work in Spanish or French (you choose)
Favorite a movie: the user should be able to mark a movie as a favorite from the detail view. Marking a movie as favorite should make that movie's information be available offline. Tapping the menu button from the details page should allow the user to add/remove favorite status. Getting to the favorites list activity should be triggered from menu button from the main page.
Advanced Graphics: In the main tableview, have the Progress Bar's color change on a gradient from green (#799C2A) to red (#AA0b00). You must add a shadow to the movie poster. You must also use a custom font instead of the built in system fonts (grab one from http://fontsquirrel.com)
Universal: Make the application a tablet application.
Reviews: Allow the user to view all the reviews of the application. Reviews should like the critic name, the freshness, publication, quote, and show the link to the entire article. The link to the article can open externally in the default browser.
Facebook: Integrate Facebook and post to the user's wall about the movie. You may not use the share intent to accomplish this task, you must use the Official Facebook SDK.
Find Theaters near you: Find the theaters nearest to the user's current location and display them on a map. You may use any API you like to get this data.

Application requirements:
Base OS: Gingerbread.
Application only needs to run in portrait orientation

------------------------

PHIL BROWN
The Nerdery Android Challenge

This archive contains the following files:

* This README file
* Nerd Tomatoes
* PullToRefresh

The best way to ensure that the project will compile correctly is to open this entire
folder as an Eclipse workspace. Nerd Tomatoes is dependent on PullToRefresh, so if this
is done differently, the path to the library needs to be changed in the project settings.

Other libraries included:

AbLE: This is my Annotation-based Layout Engine. I wrote it recently to provide a
light-weight way of creating more complex Activity content than XML can provide. I use this
engine throughout the app to handle most of my views. The project source, as well as a 
better explanation and full javadocs, is available on GitHub at http://phil-brown.github.io/AbLE/.

philbrown_coremodule, dd-plist: These are part of my core infrastructure, which includes some helpful
reusable code for layouts, logging, and networking. The plist parsing was not used in this project, but
can provide a way to read and write pList files on Android. The primary class used from this package
(may be the only one) is AsyncHttpRequest, which allows me to create and execute POST and GET HTTP requests
in an AsyncTask.

twitter4j: Used to handle OAuth authentication for Twitter, and to simplify posting tweets.

-----

Of the "choose one" tasks, I added Advanced Graphics. The font used was from fontsquirrel.com, as suggested.

Of the given Android assets, I used the ratings images, and the icon images (though I scaled down some of these
to the sizes recommended by Android at http://developer.android.com/guide/practices/ui_guidelines/icon_design_launcher.html).






