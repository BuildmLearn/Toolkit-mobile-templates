/* Copyright (c) 2012, BuildmLearn Contributors listed at http://buildmlearn.org/people/
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the BuildmLearn nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.flashcardapp.main;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation;
import android.graphics.Matrix;
import android.graphics.Camera;

public class FlipAnimation extends Animation
       {
           private Camera camera;
        
           private View fromView;
           private View toView;
        
           private float centerX;
           private float centerY;
        
           private boolean forward = true;
        
           /**
            * Creates a 3D flip animation between two views.
            *
            * @param fromView First view in the transition.
            * @param toView Second view in the transition.
            */
           public FlipAnimation(View fromView, View toView)
           {
               this.fromView = fromView;
               this.toView = toView;
        
               setDuration(700);
               setFillAfter(false);
               setInterpolator(new AccelerateDecelerateInterpolator());
           }
        
           public void reverse()
           {
        	   if(forward){
	               forward = false;
	               View switchView = toView;
	               toView = fromView;
	               fromView = switchView;
        	   }
           }
           
           public void forward(){
        	   if(!forward){
	        	   forward = true;
	               View switchView = toView;
	               toView = fromView;
	               fromView = switchView;
        	   }
           }
           
           @Override
           public void initialize(int width, int height, int parentWidth, int parentHeight)
           {
               super.initialize(width, height, parentWidth, parentHeight);
               centerX = width/2;
               centerY = height/2;
               camera = new Camera();
           }
        
           @Override
           protected void applyTransformation(float interpolatedTime, Transformation t)
           {
               // Angle around the y-axis of the rotation at the given time
               // calculated both in radians and degrees.
               final double radians = Math.PI * interpolatedTime;
               float degrees = (float) (180.0 * radians / Math.PI);
        
               // Once we reach the midpoint in the animation, we need to hide the
               // source view and show the destination view. We also need to change
               // the angle by 180 degrees so that the destination does not come in
               // flipped around
               if (interpolatedTime >= 0.5f)
               {
                   degrees -= 180.f;
                   fromView.setVisibility(View.GONE);
                   toView.setVisibility(View.VISIBLE);
               }
        
               if (forward)
                   degrees = -degrees; //determines direction of rotation when flip begins
        
               final Matrix matrix = t.getMatrix();
               camera.save();
               camera.rotateY(degrees);
               camera.getMatrix(matrix);
               camera.restore();
               matrix.preTranslate(-centerX, -centerY);
               matrix.postTranslate(centerX, centerY);
           }
       }
