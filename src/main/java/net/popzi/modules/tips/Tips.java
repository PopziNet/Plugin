package net.popzi.modules.tips;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.popzi.Main;
import net.popzi.modules.BaseModule;
import net.popzi.utils.Random;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class Tips extends BaseModule {

    public Tips(Main main) {
        super(main);
        this.startTipsTimer();
    }

    @Override
    public String getName() {
        return "MODULE_TIPS";
    }

    @Override
    public void handleEvent(Event event) {}

    /**
     * The main tips' timer.
     * Times the tips to tip at the time of tips that need tipping.
     */
    public void startTipsTimer() {
        this.main.getServer().getScheduler().runTaskTimer(this.main, () -> {
            @Nullable List<?> tips = this.main.CFG.getData().getList("TIPS");
            if (tips == null) return;
            int randInt = Random.number(0, tips.size());
            String randTip = (String) tips.get(randInt);
            this.main.getServer().broadcast(Component.text( "Tip: " + randTip).color(NamedTextColor.GRAY));
        }, 20*10L, this.main.CFG.getData().getInt("TIPS_FREQUENCY"));
    }
}
