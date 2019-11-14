+++
date = "2015-08-23"
title = "Sucky Software"
+++
A big problem with Android is that a lot of the default apps suck. I often have
to find replacements. Apple's default apps are usually wonderful. Sometimes I
still decided to find replacements that had more features, but the apps on iOS
didn't carry the same feeling of suckiness. Why?

<strong>Feature priority</strong>

The sucky apps usually have one or two missing features that, to me, are
critical. It seems obvious that the app would need to have them, and the
features usually wouldn't be that hard to implement. 

Consider the set of all features a particular app could have. Let's say the set
is sorted by priority of implementation—which features should be implemented
first. Priority is given by importance (a compound of how many people would use
the feature and how much utility they would derive from it) divided by the
amount of resources (e.g. time) required to implement the feature. A less
important feature could still have a higher priority than a more important
feature if the former is trivial to implement while the latter is more complex.

The feeling of suckiness comes in part from when it feels like features have
been implemented out of order, based on priority. Here's an example. I use my
phone to play music. There are two particular features I would like a music app
to have: 1) the option to select a specific directory that holds music, and 2)
hierarchical folder navigation (as opposed to flat folder navigation). I don't
know of any apps that have both these features. Feature 1 especially seems like
a no-brainer, yet few apps have it. The app I use right now, Shuttle Music
Player, doesn't have that feature. As a result, the folder with <a
href="https://www.lds.org/general-conference/">general conference</a>
recordings was being included in my music library. It was pretty annoying
scrolling through dozens of "artists," all with names that begin with "Elder."
It seems to me that many people would have non-music audio recordings on their
phones. Why would an app not allow me to select where my actual music library
is located so the app doesn't have to add all audio files on the entire SD
card?

But that's not all—Shuttle does a lot of fancy album artwork downloading, so
practically any song I have in the library (except for those by Elder Ucthdorf)
has associated album artwork. This is a nifty feature I guess, but it's not a
critical one. After all, it's a music player, not an art gallery.

Again, the suck is there not just because the music player doesn't have the
feature I want but because it also has other features that seem to have a lower
priority. Using an app that doesn't have a feature I want is like getting on a
bus and finding out it doesn't go as far as I wanted to go—irritating but
understandable. Using an app that doesn't have the feature I want but
<em>does</em> have other lower priority features is like getting on a bus that
drives right past my destination but doesn't stop to let me off. The first
experience is disappointing, but the second is truly sucky.

So why did the developers give higher priority to album artwork downloading
than to music library location specification? Why do developers implement
features out of order?

I'm still trying to figure that one out. There are two possibilities. I could
be miscalculating the priority of a particular feature. Perhaps most people
don't actually care about the feature as much as I assume, or in some cases,
maybe the feature would be harder to implement than I think. The other
possibility is that the developers are unaware of the feature or its priority.
Maybe they haven't even thought of that feature, or they don't realize how
important it is. The only other possibility is that the developers are in fact
aware of the feature and its priority but still choose not to implement it.
Unless Apple has hired a team of developers to embed Android with apps that
seem good but are just sucky enough to leave a bad taste in users' mouths, this
seems unlikely.

I think one reason Apple's apps tend to not suck is because they put a lot of
effort into understanding their users and what they need. They tend to have a
good grip of all the features at the top of the priority list. But there's
another aspect of app design that Apple is really good at. I call it "ergonomic
usage."

<strong>Interface</strong>

There are two parts to any app: the raw functionality and the user interface.
We've covered functionality already. A good interface could be defined as one
that allows the user to take advantage of the app's functionality with as
little effort as possible. Again, it'll be easiest to explain with an example.

I'll compare two music player apps, Poweramp and shuttle. Poweramp is a widely
used music player app for Android, and it constitutes what should be a good
example of a well-designed app. I'll compare the following tasks on both
players: <ol> <li>Start playing all the songs of a particular artist with
shuffle on.</li> <li>Navigate to a different artist.</li> <li>Navigate to an
album of a different artist.</li> <li>Create a playlist with songs from all
three previous locations.</li> </ol> <strong>Poweramp</strong>

I open the app. It loads a now-playing screen. There's no "artist" button.
There's an icon with a treble clef superimposed over a folder. That looks
promising. It leads me to a list of all the folders on my SD card with audio
files (makes sense). Still no artist button. There's a thing at the bottom that
says "Library," so I'll try that. OK, now I have a menu where I can select all
songs, albums, artists, etc. I click on "artists" and then scroll down to Rise
Against. I then get a screen with all the albums. Fortunately there's an "All
Artist Songs" button, so I click that and finally I'm at my destination. I
select the song "This is Letting Go," and then I press the shuffle icon. Task 1
completed with six clicks.

Task 2 is the same, only it takes one less click because when I click on that
first folder icon, it immediately loads the Library screen instead of the
Folders screen (why isn't folders just another option next to artists and
albums?). Navigating to an album is similar. We're up to 15 total clicks so
far.

I'm currently listening to the album Fallen by Evanescense. Fortunately I
already found out by accident that if I touch the name of the currently playing
song, it will bring me back to the song list of the album. I add three songs to
the playlist, which takes three clicks per song (there's no way to add multiple
songs at the same time). I navigate back to the artists view. I add all songs
from 3 Doors Down and then I add all the songs from Appeal to Reason, one of
Rise Against's albums.

Total clicks: 35

I ran into a few pitfalls while doing this. After finishing the first song in
Fallen while I was playing that album, instead of going to another song in the
same album, it switched to a song by a completely different artist. WTF (what
the flip)? The same thing happened later when I listened to the playlist. I
know this can be fixed in the settings—I've done it before—but it's not
straightforward at all. This is a <strong>serious</strong> flaw. I don't think
I need to state how obvious it is that when playing an album or a playlist,
most people will expect the app to <em>only play songs in that album or
playlist </em>(hint: it's pretty obvious). The number of clicks it takes to
perform all our tasks is insignificant in regards to this headache. There were
a few other little quirks that came up, but this was the worst.

<strong>Shuttle</strong>

I open the app and it brings me right to the artists page. On the top of the
screen I can see buttons for albums and songs. I already feel more at home. I
scroll down and click on Rise Against. It loads a screen that has the albums
but also has the entire song list underneath, saving me a click. Also, there's
a nifty button to immediately shuffle through all the songs, so I don't even
have to choose a starting song if I don't want to. Mission accomplished in two
clicks. Navigating to the second artist takes me one additional click because I
have to go back to the artist view. Navigating from there to Fallen takes four
clicks. Creating the exact same playlist as I did in Poweramp takes 16 clicks.

Total clicks: 25

In addition to being about 29% more efficient with regard to number of clicks
alone, Shuttle's interface just felt so nice. Everything worked as I expected
it to, and it was all laid out in a helpful way. I can't think of any way the
interface could have saved me more effort as I used the features—a good
interface by definition. Can you see why I use Shuttle? Apart from the music
folder location thing, it really is a wonderful app.

I don't think I've bashed Poweramp quite enough, so let me direct you to the <a
href="https://play.google.com/store/apps/details?id=com.maxmpz.audioplayer&amp;hl=en">Google
Play listing</a>. First of all, this app is actually just a two-week free
trial. The full version costs $3.99. As of right now the trial version has
about 900,000 downloads. The developer, Max MP, is listed as a "top developer."
Now let's take a look at <a
href="https://play.google.com/store/apps/details?id=another.music.player&amp;hl=en">Shuttle</a>.
There is a full version for $1.75, but the free version isn't a trial. 50,000
downloads. SimpleCity, the developer, looks like an average Joe.

The descriptions of each app are telling: <blockquote>Shuttle Music Player is
an intuitive, lightweight and powerful music player for Android.

Poweramp is a powerful music player for Android.</blockquote> Intuitive isn't a
buzz word. It's a great description of Shuttle. Notice that the Poweramp
description only mentions power (and that it's for Android. Same for Shuttle. I
thought it was obvious since I'm looking at the Google Play store). Poweramp
does have a lot of features, but it's hard for me to care when my head is
throbbing from the aneurysm caused by their abomination of a user interface
(alright, maybe it's not that bad, but "abomination" is one of my favorite
words).

As <a href="https://en.wikipedia.org/wiki/Eric_S._Raymond">Eric Raymond</a>
points out in a <a
href="http://www.catb.org/esr/writings/cups-horror.html">horror story</a> about
a terribly designed user interface, a "slick-looking UI" is not necessarily a
"well-designed UI."

When I was serving as a missionary in Malaysia and Singapore, I would be
transferred to a different area usually about every three to six months. I
typically had to catch a flight to the new area, so all my belongings had to be
packed into a couple of luggage cases. When I arrived at my new apartment, I
would take everything out of my luggage and arrange it so I could get to all of
my things with the least amount of effort. Clothes go in the dresser, books go
on my desk, etc. It's pretty obvious. Good design usually doesn't stand out
because it's not supposed to. The interface design should connect you with the
features as closely as possible and stay out of the way.

I heard of another missionary who was transferred to a new area six weeks
before he was scheduled to go back home to the US. He didn't want to unpack all
his things just to put them all back soon after, so he lived out of his luggage
cases for those six weeks. This is like many poorly designed interfaces. All
the functionality is there, but it's not laid out in a way that facilitates
ergonomic usage. Yet many of these interfaces come with customizable themes.
"You can choose a luggage case in black (default), blue, orange, pink, red,
green, cyan or chartreuse!" Isn't that comforting?

Now, some of these bad interface decisions might be small, but small, repeated
amounts of effort add up. You want an ergonomic design that minimizes effort.

May we all remember these principles and push for a higher standard of quality
in the software we use. I believe that both Android and desktop Linux would
benefit greatly.  I plan to develop several Android apps with these
philosophies in mind, one of which will be a music player. I think I'll call it
"SuckFree Music Player." Ideally, the app will be good enough to be considered
as a "default" app for custom ROMs, or one day even Android itself. If that day
ever comes, I only hope I don't have to compromise on a more politically
correct name.
