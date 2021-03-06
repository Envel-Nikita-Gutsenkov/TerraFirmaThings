package lyeoj.tfcthings.items;

import lyeoj.tfcthings.entity.living.EntityPigvil;
import lyeoj.tfcthings.main.ConfigTFCThings;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.api.types.IAnimalTFC;
import net.dries007.tfc.objects.entity.animal.EntityPigTFC;
import net.dries007.tfc.objects.items.ItemTFC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nonnull;

public class ItemPigIronCarrot extends ItemTFC {

    public ItemPigIronCarrot() {
        setRegistryName("pig_iron_carrot");
        setTranslationKey("pig_iron_carrot");
    }
    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack) {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack) {
        return Weight.MEDIUM;
    }

    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if(entity instanceof EntityPigTFC) {
            EntityPigTFC piggy = (EntityPigTFC)entity;
            float requiredFamiliarity = (float)ConfigTFCThings.Misc.PIGVIL.familiarityLevel;
            if(piggy.getGender().equals(IAnimalTFC.Gender.MALE) && piggy.getAge() == IAnimalTFC.Age.ADULT && piggy.getFamiliarity() >= requiredFamiliarity) {
                player.world.playSound(player, piggy.getPosition(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                if(!player.world.isRemote) {
                    if(!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    if(Math.random() < ConfigTFCThings.Misc.PIGVIL.convertChance) {
                        EntityPigvil pigvil = new EntityPigvil(entity.world);
                        pigvil.copyLocationAndAnglesFrom(piggy);
                        entity.world.spawnEntity(pigvil);
                        piggy.spawnExplosionParticle();
                        piggy.setDead();
                    }
                }
            }
        }
        return super.itemInteractionForEntity(itemstack, player, entity, hand);
    }
}
