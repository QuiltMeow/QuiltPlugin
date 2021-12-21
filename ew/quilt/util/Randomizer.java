package ew.quilt.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    private final static Random RAND = new Random();

    public static final int nextInt() {
        return RAND.nextInt();
    }

    public static final int nextInt(final int arg0) {
        return RAND.nextInt(arg0);
    }

    public static final void nextBytes(final byte[] bytes) {
        RAND.nextBytes(bytes);
    }

    public static final boolean nextBoolean() {
        return RAND.nextBoolean();
    }

    public static final double nextDouble() {
        return RAND.nextDouble();
    }

    public static final float nextFloat() {
        return RAND.nextFloat();
    }

    public static final long nextLong() {
        return RAND.nextLong();
    }

    public static final int randomDamage(int random) {
        return (RAND.nextInt() % 2 == 0 ? 1 : -1) * RAND.nextInt(random + 1);
    }

    public static final int rand(final int lbound, final int ubound) {
        return nextInt(ubound - lbound + 1) + lbound;
    }

    public static boolean isSuccess(int rate) {
        return rate > nextInt(100);
    }

    public static double nextDouble(double max) {
        return ThreadLocalRandom.current().nextDouble(max);
    }

    public static double randDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
