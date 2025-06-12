package enums;

public enum StreetName {
    AVENIDA_DAS_ACACIAS_DA_PENINSULA("Avenida das Acácias da Península"),
    AVENIDA_DOS_FLAMBOYANTS_DA_PENINSULA("Avenida dos Flamboyants da Península"),
    RUA_ANGELINS_DA_PENINSULA("Rua Angelins da Península"),
    RUA_AROEIRAS_DA_PENINSULA("Rua Aroeiras da Península"),
    RUA_BAUHINEAS_DA_PENINSULA("Rua Bauhíneas da Península"),
    RUA_BROMELIAS_DA_PENINSULA("Rua Bromélias da Península"),
    RUA_PAU_BRASIL_DA_PENINSULA("Rua Pau-Brasil da Península"),
    RUA_SAPUCAIAS_DA_PENINSULA("Rua Sapucaias da Península");

    private final String displayName;

    StreetName(String displayName) {

        this.displayName = displayName;
    }

    public String getDisplayName() {

        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}

