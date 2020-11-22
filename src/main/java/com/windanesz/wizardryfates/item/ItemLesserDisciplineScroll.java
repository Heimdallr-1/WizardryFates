package com.windanesz.wizardryfates.item;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.DisciplineMode;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import com.windanesz.wizardryfates.registry.Sounds;
import com.windanesz.wizardryfates.registry.WizardryFatesTabs;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLesserDisciplineScroll extends Item {

	private Element element;

	public ItemLesserDisciplineScroll(Element element) {
		setCreativeTab(WizardryFatesTabs.WIZARDRYFATES);
		setMaxDamage(0);
		setMaxStackSize(1);
		this.element = element;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (Settings.settings.sub_discipline_scrolls_enabled) {

			if (DisciplineMode.getActiveMode() == DisciplineMode.SUB_DISCIPLINE_MODE) {
				if (DisciplineUtils.addSecondaryDiscipline(player, element, false, player)) {

					if (!world.isRemote) {
						String elementName = Utils.getElementWithStyleFormat(element);

						if (element == Element.MAGIC) {
							elementName += " MAGIC";
						}
						player.sendMessage(new TextComponentTranslation("message.wizardryfates:discipline_granted", elementName));
						player.world.playSound(null, player.getPosition(), Sounds.LESSER_DISCIPLINE_SCROLL_USE, SoundCategory.PLAYERS, 0.7F, 1.0F);

					}
					stack.shrink(1);
					return new ActionResult<>(EnumActionResult.SUCCESS, stack);
				}
			} else {
				if (!world.isRemote) {
					player.sendMessage(new TextComponentString("This item is disabled as sub-disciplines are not enabled in the configuration!"));
				}
			}

		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack) {
		return ElementRarityEnum.fromName(element.getName());
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
		tooltip.add(Wizardry.proxy.translate("item." + getRegistryName() + ".tooltip"));
		if (!Settings.settings.sub_discipline_scrolls_enabled) {
			tooltip.add(Wizardry.proxy.translate("tooltip." + WizardryFates.MODID + ":disabled_item"));
		}
	}
}
