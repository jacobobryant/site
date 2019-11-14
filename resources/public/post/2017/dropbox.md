+++
date = "2017-04-06"
title = "I Heart Dropbox"
+++

The shift to running application software on the server side
has obviously simplified development and deployment a lot. Storing the
data server-side is a natural thing to do also. But the problem with
this setup is that the user has less control. The data is in the hands
of the application provider.

I'm working on a [music player
app](https://play.google.com/store/apps/details?id=com.jacobobryant.moody.vanilla)
for Android as part of a research project. However, the app can only
access music stored in the device's central media database. A lot of
users have their music stored away in Google Music, Amazon Music, etc.
My app doesn't work for them.

What if everyone used Dropbox to handle data syncing and web
applications didn't bother about data storage? I don't see any reason
why web applications like Google Docs couldn't operate on local files.
There may be security concerns since the applications would need to
interact more with your file system, but that shouldn't be a show
stopper. There wouldn't be a loss of convenience since Dropbox would
sync the data for you, but the user would have more control since
their data wouldn't be tied to any particular software. They wouldn't
even have to depend on Dropbox if a good competitor came along.

Dropbox does need some improvements, especially for mobile devices.
Currently (on Android, at least) the official app doesn't actually
sync the files to your phone's file system. The files are only
available from within the Dropbox app. [There is an app](
https://play.google.com/store/apps/details?id=com.ttxapps.dropsync&hl=en)
to fix that, and it works wonderfully. I can take a picture with my
phone and it immediately gets synced to the file system on my
workstation. However, it'd be better if we didn't have to rely on a
third party for this.

But as issues like that get ironed out, I think it could work
great to factor out data syncing into a single service like Dropbox.
All your data would be consolidated on your file system instead of
being spread out across a bunch of separate applications, and
developers (like me) wouldn't have to deal with implementing data
syncing.
