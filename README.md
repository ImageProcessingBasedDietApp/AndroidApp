# AndroidApp

Android App of Image Processing Based Diet Application is programmed differently from classical diet applications. The bigger difference is Image Processing
work in the project. On this project we are trying to do real time plate detection and then food detection on the plate which is detected. So we use two models for
Image Processing. 

1. ##### Plate Model: Detect the plate on real time camera view.
2. ##### Food Model: Detect type of food from the image of the plate.


Plate Model has 2 labels which are Plate and Non-Plate. Each of these classes has 7000 photos which are plates and some objects which are not plate. Food Model
has 102 labels which are traditional Turkish Foods. Each of food type has approximately 400-500 photos. We train our models with TensorFlow Lite Image Classification.


# Some screenshots from app
<p float="left">
    <img src="https://user-images.githubusercontent.com/17880147/123433168-d303f600-d5d3-11eb-858d-0566d062d28b.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123433159-d0a19c00-d5d3-11eb-9eec-b35a360d419c.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123431881-6f2cfd80-d5d2-11eb-991b-f6bf30df4a8a.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123431892-718f5780-d5d2-11eb-9bbc-cff50d7820b3.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123433497-33933300-d5d4-11eb-86f2-b73dff25432c.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123433529-3b52d780-d5d4-11eb-8826-245da5f7f2d5.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123434023-b7e5b600-d5d4-11eb-901d-6cdd164ab4e1.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123434069-c0d68780-d5d4-11eb-84b8-a0ed8a972bd3.png" width="30%" height="30%">
    <img src="https://user-images.githubusercontent.com/17880147/123434078-c3d17800-d5d4-11eb-8a45-5af3d9428551.png" width="30%" height="30%">
 </p>


