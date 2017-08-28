package iunius118.mods.handheldnavalgun.client.model;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelBakedItemOBJ implements IPerspectiveAwareModel
{

    public IBakedModel modelItem;
    public OBJBakedModel modelOBJ;
    public TransformType transformTypeCamera;
    public ItemStack item;
    public World worldObj;
    public EntityLivingBase player;

    public ModelBakedItemOBJ(IBakedModel itemBakedModel, OBJBakedModel objBakedModel)
    {
        this.modelItem = itemBakedModel;
        this.modelOBJ = objBakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        switch (this.transformTypeCamera)
        {
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
            return modelOBJ.getQuads(state, side, rand);
        default:
            return modelItem.getQuads(state, side, rand);
        }
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return modelItem.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return modelItem.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return new ItemOverrideList(Collections.emptyList()) {

            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
            {
                if (originalModel instanceof ModelBakedItemOBJ)
                {
                    ModelBakedItemOBJ model = (ModelBakedItemOBJ) originalModel;

                    if (entity != null)
                    {
                        model.item = stack;
                        model.worldObj = world;
                        model.player = entity;
                    }

                    return model;
                }

                return originalModel;
            }

        };
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        Matrix4f matrix;

        this.transformTypeCamera = cameraTransformType;

        if (modelItem instanceof IPerspectiveAwareModel)
        {
            matrix = ((IPerspectiveAwareModel) modelItem).handlePerspective(cameraTransformType).getValue();
        }
        else
        {
            matrix = TRSRTransformation.identity().getMatrix();
        }

        switch (cameraTransformType)
        {
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
            return Pair.of(this, modelOBJ.handlePerspective(cameraTransformType).getValue());

        default:
            return Pair.of(modelItem, matrix);
        }
    }

}
