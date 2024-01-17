package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;

import java.util.stream.Stream;

public class Propagation extends AbstractSMOCard {

  public static String ID = SpireAnniversary6Mod.makeID("Propagation");

  public Propagation() {
    super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY, CardColor.COLORLESS);
    this.baseMagicNumber = this.magicNumber = 0;
    this.baseDamage = this.damage = 2;
    this.exhaust = true;
  }

  @Override
  public void upgrade() {}

  @Override
  public void upp() {}

  @Override
  public void applyPowers() {
    super.applyPowers();
    magicNumber = baseMagicNumber + findCandidates();
    if (magicNumber > 0) {
      rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1];
      initializeDescription();
      isMagicNumberModified = magicNumber != baseMagicNumber;
    } else {
      rawDescription = cardStrings.DESCRIPTION;
      initializeDescription();
    }
  }

  public void onMoveToDiscard() {
    rawDescription = cardStrings.DESCRIPTION;
    initializeDescription();
  }

  private int findCandidates() {
    return Stream.of(Wiz.p().hand, Wiz.p().drawPile, Wiz.p().discardPile).map(group -> group.group.stream().filter(this::itCounts).count()).mapToInt(Long::intValue).sum();
  }

  private boolean itCounts(AbstractCard card) {
    return card.cardID.equals(Slimed.ID) || card.cardID.equals(ID);
  }

  public void triggerWhenDrawn() {
    addToTop(new MakeTempCardInDrawPileAction(makeStatEquivalentCopy(), 1, true, true));
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (int i = 0; i < magicNumber; ++i) {
      Wiz.atb(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON, true));
    }
  }
}
