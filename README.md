##Social Mash Up - Twitter

This is a one of the module in the social-mashup application. This is a fun application to
demonstrate the capabilities of React with Streaming Rest APIs. This requires react-social-mashup module 
to be run as well.

When you got both application up and running. You can type topic and receive live feeds
from Twitter. A maximum of 2 Topics can be fit into the user interface. Off course the user
interface is kept bare minimum. 

This module uses Twitter [HBC-Core](https://github.com/twitter/hbc) and [Web-Flux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) 
to acheive Rest Streaming for publishing live streaming of twitter feeds.

Since Twitter Developer account is basic and free of charge, only 1 % of a topic (foe example #Trump) 
would come out of the API.

Requirements

Java 8
Gradle
Java IDE

## Running the Application

Get a Twitter API Account created and fill the application.properties.

twitter.apiKey = 
twitter.apiSecret = 
twitter.token = 
twitter.secret = 


java -jar social-mashup.jar com.usage.spring.social.socialmashup.SocialMashupApplication


