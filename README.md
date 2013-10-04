cIFS
==========================
A very simple android app which allows to visualize Möbius-tranformations by iterative function systems.

General idea
------------
A starting point and a set of transformations, which take as input a single point and output another point, are given.
Then, the following simple iteration is applied X-times:

	X-times do
		select transformation t from the set
		apply transformation z_new = t(z_old)
		plot(z_new)
		z_old = z_new
    
A transformation can be for example a scaling, rotation, inversion, or a combination and is defined by a set of points and their relations, see below.
    
Usage
-----
In the image below, the starting point is in green and the menu offers actions to do:
* add a point
* add a P2 transformation
* add a inversion transformation

![Start screen](http://i.imgur.com/rjUILtv.png?1)

Given four points A,B,C,D (which do not have to be pairwise different), a P2 transformations is a mapping:

	(A,C) -> (B,D)
	
i.e. A is mapped onto B and C onto D.
As an example, see the next figure:

![Transformation](http://i.imgur.com/nx3x9ML.png?1)

Here, A is mapped onto itself, and B onto C. Applying the transformation results in a picture like the following:

![Applied Transformation](http://i.imgur.com/7dxzpzP.png?1)

Changing the transformation (which is defined by its points A,B,C) by moving around the white points will result in a new image.

As the transformation might be sensitive to changes, a point can be selected by touching it for 1s and a lower panel of buttons emerges, which allows to move the point pixelwise.

The preferences menu allows to change some default parameters (e.g. number of iterations, color of samples, ...)
