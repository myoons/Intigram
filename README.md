# Intigram

App Description : Service Finding "Real Friends" by Callogs and Images.
Application composed of four tabs.

1. First Tab : Home 
Description about the Application.
Just for Design.

2. Second Tab : Address
Show the phone numbers that are stored in local storage.
Uses Hash Function to delete same numbers. (Due to Google storage and local storage)
--> Find the characteristic of thumbnails.
Two characteristics are calculated by using gooble Face Detection library

Ratio between distance of two eyes and distance of cheeks.
Ratio between distance of two eyes and distance of mouth.

Look for the callogs.
More record, higher points. 
--> Calculates the point between me and my friend.

3. Third Tab : Gallery
Can bring images in local gallery, google gallery, camera.
For each picure find the characteristics above. (For each face)

Compare with the characteristics between gallery thumbnail.
Find who took a picture with.

For example, if David and Park were in same image, they have points.
--> Callculates the point between people. (Using address thumbnail and gallery images)

4. Intigram (Main Service)

Show the relationship between people.
Higher points, more width line between two people.
Show top 5 person and its thumbnail. (Like a graph)
