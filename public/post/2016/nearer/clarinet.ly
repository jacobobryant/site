\version "2.18.2"

\header {
  title = "Nearer, Dear Savior, to Thee"
  instrument = "Bb clarinet"
  composer = "Music: William Clayson"
  arranger = \markup { \italic "arr." "Jacob O'Bryant" }
  copyright = \markup \tiny \center-column {
    "Adapted from an arrangement by Andrew Hawryluk (www.musicbyandrew.ca)"
    "You can share and modify this work under the Creative Commons Attribution-Noncommercial 3.0 License."
    "To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/3.0/us"
  }
  tagline = ""
}
melody = {
  \clef "treble"
  \key f \major
  \time 6/8
  \set Score . skipBars = ##t
  R2.*8
  
  a'4. f'4 a'8 c''4. a'4 g'8 f'2. ~ f'4. r
  f' d' bes' a'4 f'8 g'2. ~ g'4. r
  a' f'4 a'8 c''4. d''4 bes'8 a'2. ~
  a'4. g'4 f'8 g'4. a'4 g'8 f'2. ~ f'4. r
  
  \mark \default
  c'' bes'4 a'8 g'4. c''4 bes'8 a'2. ~ a'4. r
  bes' a'4 g'8 c''4. bes'4 a'8 g'2. ~ g'4. r
  c'' a'4( c''8) f''4.( e''4) d''8 c''4. ~ c''4 a'8 c''2.
  a'4. f'4 a'8 g'4. a'4 g'8
  
  % verse 2
  \mark \default \key des \major
  f'4. des'4 f'8 aes'4. f'4 ees'8 des'2. ~
  des'4. bes \breathe ges' f'4 des'8 ees'2.
  f'4. des'4 f'8 aes'4. bes'4 ges'8 f'2. ~
  f'4. ees'4 des'8 ees'4. f'4 ees'8 des'2. ~ des'4. r
  
  \mark \default
  aes' ges'4 f'8 ees'4. aes'4 ges'8 f'2. ~ f'4. r
  ges' f'4 ees'8 aes'4. ges'4 f'8 ees'2. ~ ees'4. r
  aes' f'8.[ aes'] des''4.( c''4\fermata) bes'8 aes'4. ~ aes'4 f'8 aes'2.
  f'4. des'4 f'8 ees'4. f'4 ees'8 des'2. ~ des'4. r
  
  \mark \default
  aes' ges'4 f'8 ees'4. aes'4 ges'8 f'2. ~ f'4. r
  ges' f'4 ees'8 aes'4. ges'4 f'8 ees'4.( ges' ~ ges' aes')
  
  % verse 3
  \key fis \major
  \mark \default
  ais'4 \tuplet 3/2 { gis'16 fis' eis' } fis'4 ais'8 cis''4. ais'4 gis'16[ eis'] fis'2. ~ fis'4. r
  fis' dis'8[ b dis'] b'4. ais'8.[ gis'16 fis' ais'] gis'2. ~ gis'4. r8
  \tuplet 6/4 { b16 cis' dis' eis' fis' gis' } ais'4. fis'4 ais'8 cis''4. eis''4 dis''16[ eis''] fis''4. ~ fis''8 r4
  ais''4. gis''4 fis''8 gis''4 fis''16[ gis''] ais''4 gis''8 fis''2. ~ fis''4. r
  
  \mark \default
  cis'' b'4 ais'8 gis'4. cis''4 b'8 ais'2. ~ ais'4. r
  b' ais'4 gis'8 cis''4. b'4 ais'8 gis'2. ~ gis'4. r
  cis'' ais'4( cis''8) fis''4.( eis''4) dis''8 cis''4. ~ cis''4 ais'8 fis'2.
  ais'4. fis'4 ais'8 gis'4. ais'4 gis'8 fis'2. ~ fis'8 r4
  ais4. b2. ~ b4. b ais2. ~ ais ~ ais
}

\score {
  \new Staff \melody
  \layout { }
  \midi { }
}
