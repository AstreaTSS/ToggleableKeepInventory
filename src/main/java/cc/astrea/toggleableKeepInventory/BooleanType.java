package cc.astrea.toggleableKeepInventory;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

// taken from 1.21 paper
class BooleanPersistentDataType implements PersistentDataType<Byte, Boolean> {

    @NotNull
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @NotNull
    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @NotNull
    @Override
    public Byte toPrimitive(@NotNull Boolean complex, @NotNull PersistentDataAdapterContext context) {
        return (byte) (complex ? 1 : 0);
    }

    @NotNull
    @Override
    public Boolean fromPrimitive(@NotNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
        return primitive != 0;
    }
}

public class BooleanType {
    public static PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanPersistentDataType();
}
