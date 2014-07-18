package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

public class StageScene extends Scene
{
  protected float            _stage_time_in_seconds    = 0.0f;
  protected int              _count_of_continue_ticket = 0;
  protected float            _death_height             = -5.0f;
  protected boolean          _pause                    = false;
  protected PlayerGameObject _player                   = null;
  protected FieldGameObject  _field                    = null;

  public StageScene( final SceneManager s )
  {
    super( s );

    push( _player = new PlayerGameObject( this, input_manager ) );
    this.camera = new StageCamera( this );
  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    if ( ! _pause )

      _stage_time_in_seconds += delta_time_in_seconds;

    super.update( delta_time_in_seconds );

    _update_collision();

    if ( _player.position.getY() <= death_height() )
    {
      if ( --_count_of_continue_ticket <= 0 )
        // シーン終了
        this.to_exit = true;
      else
        // TODO: コンティニュー処理
        ;
    }
  }

  public float death_height()
  { return  _death_height; }

  protected void _update_collision()
  {
    for ( int na = 0; na < game_objects.size(); ++na )
      for ( int nb = na + 1; nb < game_objects.size(); ++nb )
        _collision(game_objects.get( na ),game_objects.get( nb ));

  }

  protected void _collision(GameObject a, GameObject b)
  {
    if ( a instanceof RigidBodyGameObject && b instanceof RigidBodyGameObject )
      _collision_body_body( ( RigidBodyGameObject )a, ( RigidBodyGameObject )b );
    else if ( a instanceof RigidBodyGameObject && b instanceof FieldGameObject )
      _collision_body_field( ( RigidBodyGameObject )a, ( FieldGameObject )b);
    else if ( b instanceof RigidBodyGameObject && a instanceof FieldGameObject )
      _collision_body_field( ( RigidBodyGameObject )b, ( FieldGameObject )a);

  }

  protected void _collision_body_body(RigidBodyGameObject a,RigidBodyGameObject b)
  {
    for ( BoundingSphere ab : a.collision_boundings )
      for ( BoundingSphere bb : b.collision_boundings )
        if ( ab.intersect_bool( bb ) )
        {
          // 当たり処理
          a.forces.add( b.velocity.cross( b.velocity ).multiply( b.mass * 0.5f ) );
          b.forces.add( a.velocity.cross( a.velocity ).multiply( a.mass * 0.5f ) );
        }
  }

  protected void _collision_body_field( RigidBodyGameObject a, FieldGameObject b )
  {
    for ( BoundingSphere ab : a.collision_boundings )
    {
      // a の位置におけるフィールドの整数座標値
      final int field_x = (int)a.position.getX();
      final int field_z = (int)a.position.getZ();

      // a がフィールド外の x または z 座標に居る場合は判定をスキップ
      if ( field_x < 0 || field_x >= b.length_x() || field_z < 0 || field_z >= b.length_z() )
        continue;

      // Y 判定
      {
        // フィールドは足元にのみ存在する
        final float field_y = b.field_planes.get( field_x ).get( field_z );
        final Vec3 floor_position = new Vec3( a.position.getX(), field_y, a.position.getZ() );

        final float distance = ab.intersect_field( floor_position );

        if ( distance <= 0.0f )
        {
          //a.forces.add( new Vec3( 0.0f, -0.5f * a.mass * a.velocity.getY() * a.velocity.getY(), 0.0f ) );
          a.velocity = new Vec3( a.velocity.getX(), -0.3f * a.velocity.getY(), a.velocity.getZ() );
          a.position = a.position.subtract( new Vec3( 0.0f, distance, 0.0f ) );
        }
      }

      // X
      //   フィールドにより存在する可能性のある段差（壁）は x ± 1 に存在する可能性がある
      //   但し、フィールドの最も外側のセルに a が存在する場合はそれよりも外の座標のテストはスキップ
      {
        // X + 1
        if ( field_x < b.length_x() - 2 )
        {
          final int field_xm = field_x + 1;
          final float field_y = b.field_planes.get( field_xm ).get( field_z );

          final Vec3 wall_position = new Vec3( field_xm, 0, field_z );
          final Vec3 wall_normal   = new Vec3( -1.0f, 0.0f, 0.0f );

          final float distance = ab.intersect_field( wall_position, wall_normal );

          // もし壁が無限の高さをもっているのなら当たっているか判定
          // &&
          // もし壁の高さが当たり判定球の中心座標以下ならば、当たり判定とする。
          // これは厳密ではないが、たぶんゲーム内では差し当たりはそこそこ上手く行く。
          if ( distance <= 0.0f && field_y <= ab.position().getY() )
          {
            a.velocity = new Vec3( -a.velocity.getX(), a.velocity.getY(), a.velocity.getZ() );
            a.position = a.position.add( new Vec3( distance, 0.0f, 0.0f ) );
          }
        }
        // X - 1
        if ( field_x > 0 )
        {
          final int field_xm = field_x - 1;
          final float field_y = b.field_planes.get( field_xm ).get( field_z );

          final Vec3 wall_position = new Vec3( field_xm, 0, field_z );
          final Vec3 wall_normal   = new Vec3( +1.0f, 0.0f, 0.0f );

          final float distance = ab.intersect_field( wall_position, wall_normal );

          if ( distance <= 0.0f && field_y <= ab.position().getY() )
          {
            a.velocity = new Vec3( -a.velocity.getX(), a.velocity.getY(), a.velocity.getZ() );
            a.position = a.position.subtract( new Vec3( distance, 0.0f, 0.0f ) );
          }
        }
      }

      // Z
      //   X と同様
      {
        // Z + 1
        if ( field_z < b.length_z() - 2 )
        {
          final int field_zm = field_z + 1;
          final float field_y = b.field_planes.get( field_x ).get( field_zm );

          final Vec3 wall_position = new Vec3( field_x, 0, field_zm );
          final Vec3 wall_normal   = new Vec3( 0.0f, 0.0f, -1.0f );

          final float distance = ab.intersect_field( wall_position, wall_normal );

          if ( distance <= 0.0f && field_y <= ab.position().getY() )
          {
            a.velocity = new Vec3( a.velocity.getX(), a.velocity.getY(), -a.velocity.getZ() );
            a.position = a.position.add( new Vec3( 0.0f, 0.0f, distance ) );
          }
        }
        // X - 1
        if ( field_z > 0 )
        {
          final int field_zm = field_z - 1;
          final float field_y = b.field_planes.get( field_x ).get( field_zm );

          final Vec3 wall_position = new Vec3( field_x, 0, field_zm );
          final Vec3 wall_normal   = new Vec3( 0.0f, 0.0f, +1.0f );

          final float distance = ab.intersect_field( wall_position, wall_normal );

          if ( distance <= 0.0f && field_y <= ab.position().getY() )
          {
            a.velocity = new Vec3( a.velocity.getX(), a.velocity.getY(), -a.velocity.getZ() );
            a.position = a.position.subtract( new Vec3( 0.0f, 0.0f, distance ) );
          }
        }
      }
    }
  }

}