package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

/**
 * Created by sakamoto on 2014/05/14.
 */
public class RigidBodyGameObject
  extends GameObject
{
  public float mass;
  public Vec3 velocity;
  public ArrayList<Vec3> forces;

}
