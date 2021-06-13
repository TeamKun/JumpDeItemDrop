package net.kunmc.lab.jumpdeitemdrop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.Random;

public class ItemLogic {

    /**
     * @param team ture:チームに所属,false:チームに非所属
     * @return b=true:ランダムのブロック以外のアイテム,b=false:ランダムのブロック
     */
    static ItemStack dropItem(boolean team){
        Random r = new Random();
        Material m;
        if(team){
            m = JumpDeItemDrop.itemList.get(r.nextInt(JumpDeItemDrop.itemList.size()-1));
        }else{
            m = JumpDeItemDrop.blockList.get(r.nextInt(JumpDeItemDrop.blockList.size()-1));
        }

        return new ItemStack(m);
    }

}
