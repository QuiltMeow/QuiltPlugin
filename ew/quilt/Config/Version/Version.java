package ew.quilt.Config.Version;

public enum Version {
    v1_7_R1(),
    v1_7_R2(),
    v1_7_R3(),
    v1_7_R4(),
    v1_8_R1(),
    v1_8_R2(),
    v1_8_R3(),
    v1_9_R1(),
    v1_9_R2(),
    v1_10_R1(),
    v1_11_R1(),
    v1_11_R2(),
    v1_11_R3(),
    v1_12_R1(),
    v1_12_R2(),
    v1_12_R3(),
    v1_13_R1(),
    v1_13_R2(),
    v1_13_R3();

    private Integer value = null;
    private String shortVersion = null;

    public Integer getValue() {
        if (value == null) {
            try {
                value = Integer.valueOf(this.name().replaceAll("[^\\d.]", ""));
            } catch (Exception ex) {
            }
        }
        return value;
    }

    public String getShortVersion() {
        if (shortVersion == null) {
            shortVersion = this.name().split("_R")[0];
        }
        return shortVersion;
    }

    public boolean isHigher(Version version) {
        return getValue() > version.getValue();
    }

    public boolean isLower(Version version) {
        return getValue() < version.getValue();
    }
}
