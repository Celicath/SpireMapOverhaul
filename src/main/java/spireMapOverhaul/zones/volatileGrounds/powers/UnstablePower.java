package spireMapOverhaul.zones.volatileGrounds.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class UnstablePower extends AbstractSMOPower {
    public static final String ID = makeID("volUnstablePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public UnstablePower(AbstractCreature owner, int amount) {
        super(ID, NAME, PowerType.BUFF, true, owner, amount);
        updateDescription();
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }
    
    
    public void duringTurn() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.damage(new DamageInfo(owner, UnstablePower.this.amount, DamageInfo.DamageType.THORNS));
                for (AbstractMonster target : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    target.tint.color.set(Color.RED);
                    target.tint.changeColor(Color.WHITE.cpy());
                    target.damage(new DamageInfo(owner, UnstablePower.this.amount, DamageInfo.DamageType.THORNS));
                    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                        AbstractDungeon.actionManager.clearPostCombatActions();
                    }
                }
                isDone = true;
            }
        });
    }
}
