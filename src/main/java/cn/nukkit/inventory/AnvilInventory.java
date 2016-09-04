package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Position;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AnvilInventory extends ContainerInventory {

    public static final int TARGET = 0;
	public static final int SACRIFICE = 1;
	public static final int RESULT = 2;

    public AnvilInventory(Position position) {
        super(null, InventoryType.get(InventoryType.ANVIL));
        this.holder = new FakeBlockMenu(this, position);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu) this.holder;
    }

    public boolean onRename(Player player, Item resultItem) {
        Item local = getItem(TARGET);
        Item second = getItem(SACRIFICE);

        if(!resultItem.deepEquals(local, true, false) || resultItem.getCount() != local.getCount()){
            //Item does not match target item. Everything must match except the tags.
            return false;
        }

        if(local.getId() != 0 && second.getId() == 0){ //only rename
            local.setCustomName(resultItem.getCustomName());
            setItem(RESULT, local);
            player.getInventory().addItem(local);
            clearAll();
            player.getInventory().sendContents(player);
            sendContents(player);
            return true;
        } else if(local.getId() != 0 && second.getId() != 0){
            if(!local.equals(second, true, false)) {
                return false;
            }

            if(local.getId() != 0 && second.getId() != 0){
                Item result = local.clone();
                int enchants = 0;

                for(Enchantment enchantment : local.getEnchantments()){
                    Enchantment enchantment1 = second.getEnchantment(enchantment.getId());

                    if(enchantment1 != null){
                        if(enchantment.getLevel() == enchantment1.getLevel()){
                            enchantment.setLevel(enchantment.getLevel() + 1);
                        } else {
                            enchantment.setLevel(Math.max(enchantment.getLevel(), enchantment1.getLevel()));
                        }

                        if(enchantment.getLevel() > enchantment.getMaxLevel()){
                            enchantment.setLevel(enchantment.getMaxLevel());
                        }
                    }

                    result.addEnchantment(enchantment);
                    enchants++;
                }

                result.setCustomName(resultItem.getCustomName());

                player.getInventory().addItem(result);
                clearAll();
                player.getInventory().sendContents(player);
                sendContents(player);
                return true;
            }
        }

        return false;
    }

    private int[] additionalEnchants = new int[]{Enchantment.ID_DURABILITY, Enchantment.ID_BOW_FLAME, Enchantment.ID_FIRE_ASPECT, Enchantment.ID_KNOCKBACK, Enchantment.ID_BOW_KNOCKBACK, Enchantment.ID_BOW_INFINITY, Enchantment.ID_THORNS, Enchantment.ID_WATER_BREATHING, Enchantment.ID_WATER_WALKER, Enchantment.UD_WATER_WORKER, Enchantment.ID_LOOTING, Enchantment.ID_SILK_TOUCH, Enchantment.ID_FORTUNE_DIGGING};

    @Override
    public void onSlotChange(int index, Item before) {
        /*if(index == RESULT) {
            this.clearAll();
            return;
        }

        Item first = getItem(TARGET);
        Item second = getItem(SACRIFICE);

        if(!first.equals(second, true, false)) {
            return;
        }

        if(first.getId() != 0 && second.getId() != 0){
            Item result = first.clone();
            int enchants = 0;

            for(Enchantment enchantment : first.getEnchantments()){
                Enchantment enchantment1 = second.getEnchantment(enchantment.getId());

                if(enchantment1 != null){
                    if(enchantment.getLevel() == enchantment1.getLevel()){
                        enchantment.setLevel(enchantment.getLevel() + 1);
                    } else {
                        enchantment.setLevel(Math.max(enchantment.getLevel(), enchantment1.getLevel()));
                    }

                    if(enchantment.getLevel() > enchantment.getMaxLevel()){
                        enchantment.setLevel(enchantment.getMaxLevel());
                    }
                }

                result.addEnchantment(enchantment);
                enchants++;
            }

            if(first.hasCustomName()){
                result.setCustomName(first.getCustomName());
            } else if(second.hasCustomName()){
                result.setCustomName(second.getCustomName());
            }

            setItem(RESULT, result);
        }*/
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        who.craftingType = 0;

        for (int i = 0; i < 2; ++i) {
            this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), this.getItem(i));
            this.clear(i);
        }
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        who.craftingType = 3;
    }
}
