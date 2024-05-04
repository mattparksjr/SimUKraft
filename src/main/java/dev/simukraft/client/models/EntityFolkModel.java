package dev.simukraft.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

public class EntityFolkModel extends HumanoidModel<EntityFolk> {

    private final List<ModelPart> parts;

    public EntityFolkModel(ModelPart root) {
        super(root, RenderType::entityTranslucent);
        this.parts = root.getAllParts().filter((parts) -> !parts.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    public static MeshDefinition createMesh(CubeDeformation pCubeDeformation, boolean pSlim) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(pCubeDeformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        return meshdefinition;
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of());
    }
}
