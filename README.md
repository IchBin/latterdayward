# LatterDayWard

API backend for [LatterDayWard.com](https://latterDayWard.com) and other sites.

This is a backend content management system and API built to work specifically with [ourwardapp.com](https://github.com/snicol21/ourwardapp) frontend application using NextJS. 

If you'd like to create an account to host your own Ward website, please visit https://api.latterdayward.com.
Instructions on how to use this are given after logging in. 

Feel free to submit pull requests if you'd like to add a feature or make the code better! If you can't code you can still submit feature requests for discussion/addition. 

If you'd like to actually run this code on your own you are free to do that. You will need to have several properties in order to run. 
As well as a mongodb server. Feel free to contact me if you'd like to do this. I'm not going to explain this here at the moment. 

### Build notes
With the addition of TailwindCSS I have added the org.siouan.frontend-jdk11 plugin.
This will scan the template files for classes used by Tailwind and then compile only those used classes into the main.css file.