import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.neoforged.neoforge.client.model.data.ModelData;

public class TestModelRenderer {
    public void test() {
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        // check if method exists
    }
}
