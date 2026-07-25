package dmr.DragonMounts.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dmr.DragonMounts.client.model.DragonEggModel;
import dmr.DragonMounts.client.model.DragonEggModel.Baked;
import dmr.DragonMounts.config.ClientConfig;
import dmr.DragonMounts.server.blockentities.DMREggBlockEntity;
import dmr.DragonMounts.server.blocks.DMREggBlock;
import dmr.DragonMounts.types.dragonBreeds.DragonHybridBreed;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.neoforge.client.RenderTypeHelper;

public class DragonEggRenderer implements BlockEntityRenderer<DMREggBlockEntity> {

    @Override
    public void render(
            DMREggBlockEntity blockEntity,
            float v,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            int i1) {
        if (!ClientConfig.RENDER_HATCHING_EGG) {
            return;
        }

        if (!blockEntity.getBlockState().getValue(DMREggBlock.HATCHING)) {
            return;
        }

        var model = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockEntity.getBlockState());

        poseStack.pushPose();
        var time = blockEntity.tickCount;
        var breed = blockEntity.getBreed();
        int hatchTimeMax = breed != null ? breed.getHatchTime() : 1;
        float hatchProgress = ((float) blockEntity.getHatchTime() / hatchTimeMax);
        float oscillationPeriod = 100;
        float angle = (float) Math.sin((time % oscillationPeriod) * ((2 * Math.PI) / oscillationPeriod))
                * (2 + (5 * hatchProgress));

        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotateAround(Axis.XN.rotationDegrees(angle), 0, 0, 0);
        poseStack.translate(-0.5, 0, -0.5);

        var renderTypeSet = model.getRenderTypes(
                blockEntity.getBlockState(),
                blockEntity.getLevel().random,
                blockEntity.getModelData());
        var renderType = RenderTypeHelper.getEntityRenderType(
                renderTypeSet.isEmpty() ? net.minecraft.client.renderer.RenderType.solid() : renderTypeSet.asList().getFirst(),
                true);

        Minecraft.getInstance()
                .getBlockRenderer()
                .getModelRenderer()
                .renderModel(
                        poseStack.last(),
                        multiBufferSource.getBuffer(renderType),
                        blockEntity.getBlockState(),
                        model,
                        1.0f,
                        1.0f,
                        1.0f,
                        i,
                        OverlayTexture.NO_OVERLAY,
                        blockEntity.getModelData(),
                        renderType);
        poseStack.popPose();
    }
}
