package soy.frank.flutterby;

import soy.frank.flutterby.actors.PhysicalEntity;

class CollisionDetector {
    public static boolean collides(PhysicalEntity rect1, PhysicalEntity rect2) {
        return rect1.getPosition().getX() < rect2.getPosition().getX() + rect2.getWidth() &&
                rect1.getPosition().getX() + rect1.getWidth() > rect2.getPosition().getX() &&
                rect1.getPosition().getY() < rect2.getPosition().getY() + rect2.getHeight() &&
                rect1.getHeight() + rect1.getPosition().getY() > rect2.getPosition().getY();
    }
}
