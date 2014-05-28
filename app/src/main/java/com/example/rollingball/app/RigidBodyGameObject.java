package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

public class RigidBodyGameObject extends GameObject
{
  public float mass;
  public Vec3 velocity;
  public ArrayList<Vec3> forces;
  public ArrayList<Float> collision_radiuses;
}
