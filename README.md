# AndroidApp

Android App of Image Processing Based Diet Application is programmed differently from classical diet applications. The bigger difference is Image Processing
work in the project. On this project we are trying to do real time plate detection and then food detection on the plate which is detected. So we use two models for
Image Processing. 

1. ##### Plate Model: Detect the plate on real time camera view.
2. ##### Food Model: Detect type of food from the image of the plate.


Plate Model has 2 labels which are Plate and Non-Plate. Each of these classes has 7000 photos which are plates and some objects which are not plate. Food Model
has 102 labels which are traditional Turkish Foods. Each of food type has approximately 400-500 photos. We train our models with TensorFlow Lite Image Classification.
