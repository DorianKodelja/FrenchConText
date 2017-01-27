package org.context.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class used to analyze concept context for French (adapted from the 'ConText' algorithm by Chapman et al.)
 * @author Amine Abdaoui
 * LIRMM, University of Montpellier 2017
 */
public class ConTextFrench implements ConText {

	public enum NegationContext{
		Affirmed, Negated, Possible;
	}
	public enum TemporalityContext{
		Recent, Historical, Hypothetical;
	}
	
	private static final int MAX_WINDOW = 15;
	
	String[] regexes = new String[]{"adéquat pour l'écarter ,post,neg",
"adéquat pour l'éliminer ,post,neg",
"suffisant pour l'écarter ,post,neg",
"suffisant pour l'exclure ,post,neg",
"suffisant pour l'éliminer ,post,neg",
"suffisants pour l'écarter ,post,neg",
"suffisants pour l'exclure ,post,neg",
"suffisants pour l'éliminer ,post,neg",
"suffisante pour l'écarter ,post,neg",
"suffisante pour l'éliminer ,post,neg",
"suffisante pour l'exclure ,post,neg",
"suffisantes pour l'écarter ,post,neg",
"suffisantes pour l'éliminer ,post,neg",
"suffisantes pour l'exclure ,post,neg",
"adéquat pour écarter ,pre,neg",
"adéquat pour éliminer ,pre,neg",
"suffisant pour écarter ,pre,neg",
"suffisant pour exclure ,pre,neg",
"suffisant pour éliminer ,pre,neg",
"suffisants pour écarter ,pre,neg",
"suffisants pour exclure ,pre,neg",
"suffisants pour éliminer ,pre,neg",
"suffisante pour écarter ,pre,neg",
"suffisante pour éliminer ,pre,neg",
"suffisante pour exclure ,pre,neg",
"suffisantes pour écarter ,pre,neg",
"suffisantes pour éliminer ,pre,neg",
"suffisantes pour exclure ,pre,neg",
"bien que ,termin,neg",
"quoique ,termin,neg",
"bien qu',termin,neg",
"quoiqu',termin,neg",
"mais ,termin,neg",
"tout autre,pre,neg",
"n'importe quel autre,pre,neg",
"en dehors de ,termin,neg",
"hormis ,termin,neg",
"hors ,termin,neg",
"sauf ,termin,neg",
"à part ,termin,neg",
"outre que ,termin,neg",
"sont exclus ,post,neg",
"sont exclues ,post,neg",
"comme cause de ,termin,neg",
"comme cause d',termin,neg",
"comme une cause de ,termin,neg",
"comme une cause d',termin,neg",
"comme étiologie pour ,termin,neg",
"comme étiologie de ,termin,neg",
"comme une étiologie de ,termin,neg",
"comme conséquence de ,termin,neg",
"comme une conséquence de ,termin,neg",
"comme étiologie d',termin,neg",
"comme une étiologie d'e ',termin,neg",
"comme conséquence d',termin,neg",
"comme une conséquence d,termin,neg",
"comme raison pour ,termin,neg",
"comme une raison de ,termin,neg",
"en raison de ,termin,neg",
"comme une raison d',termin,neg",
"en raison d',termin,neg",
"comme cause secondaire de ,termin,neg",
"comme une cause secondaire de ,termin,neg",
"comme cause secondaire d',termin,neg",
"comme une cause secondaire d',termin,neg",
"comme étiologie secondaire pour ,termin,neg",
"comme conséquence secondaire de ,termin,neg",
"comme une étiologie secondaire de ,termin,neg",
"comme une conséquence secondaire de ,termin,neg",
"comme conséquence secondaire d',termin,neg",
"comme une étiologie secondaire d',termin,neg",
"comme une conséquence secondaire d',termin,neg",
"comme origine secondaire pour ,termin,neg",
"comme une origine secondaire pour ,termin,neg",
"comme une origine secondaire de ,termin,neg",
"comme origine secondaire de ,termin,neg",
"comme une origine secondaire d',termin,neg",
"comme origine secondaire d',termin,neg",
"comme une raison secondaire pour ,termin,neg",
"comme raison secondaire pour ,termin,neg",
"comme une raison secondaire de ,termin,neg",
"comme une raison secondaire d',termin,neg",
"comme source secondaire pour ,termin,neg",
"comme une source secondaire de ,termin,neg",
"comme une source secondaire d',termin,neg",
"comme source secondaire de ,termin,neg",
"comme source de ,termin,neg",
"comme une source de ,termin,neg",
"comme une source d',termin,neg",
"comme origine pour ,termin,neg",
"comme une origine pour ,termin,neg",
"comme une origine de ,termin,neg",
"comme une origine d',termin,neg",
"comme origine de ,termin,neg",
"comme une raison pour ,termin,neg",
"comme une étiologie secondaire pour ,termin,neg",
"comme une conséquence secondaire pour ,termin,neg",
"comme conséquence secondaire pour ,termin,neg",
"comme une source secondaire pour ,termin,neg",
"comme source pour ,termin,neg",
"comme requis,pre,hypo",
"comme cause pour ,termin,neg",
"ne cause pas ,pseudo,neg",
"ne causent pas ,pseudo,neg",
"comme une cause pour ,termin,neg",
"comme l'étiologie de ,termin,neg",
"comme l'étiologie d',termin,neg",
"comme l'étiologie pour ,termin,neg",
"comme l'origine pour ,termin,neg",
"comme l'origine de ,termin,neg",
"comme l'origine d',termin,neg",
"comme la raison pour ,termin,neg",
"comme la raison de ,termin,neg",
"comme la raison d',termin,neg",
"comme la cause secondaire pour ,termin,neg",
"comme la cause secondaire de ,termin,neg",
"comme la cause secondaire d',termin,neg",
"comme l'étiologie secondaire pour ,termin,neg",
"comme l'étiologie secondaire de ,termin,neg",
"comme l'étiologie secondaire d',termin,neg",
"comme l'origine secondaire pour ,termin,neg",
"comme l'origine secondaire de ,termin,neg",
"comme l'origine secondaire d',termin,neg",
"comme la raison secondaire pour ,termin,neg",
"comme la raison secondaire de ,termin,neg",
"comme la raison secondaire d',termin,neg",
"comme la source secondaire pour ,termin,neg",
"comme la source secondaire de ,termin,neg",
"comme la source secondaire d',termin,neg",
"comme la source pour ,termin,neg",
"comme la cause de ,termin,neg",
"comme la cause d',termin,neg",
"comme la source de ,termin,neg",
"comme la source d',termin,neg",
"excepté ,termin,neg",
"tante,pre,exp",
"grand-tante,pre,exp",
"être écarté ,post,poss",
"être écartée ,post,poss",
"être écartés ,post,poss",
"être écartées ,post,poss",
"être éliminé ,post,poss",
"être éliminée ,post,poss",
"être éliminés ,post,poss",
"être éliminées ,post,poss",
"être infirmé ,post,poss",
"être infirmée ,post,poss",
"être infirmés ,post,poss",
"être infirmées ,post,poss",
"être exclu ,post,poss",
"être exclue ,post,poss",
"être exclus ,post,poss",
"être exclues ,post,poss",
"être exclue pour ,pre,poss",
"être exclu pour ,pre,poss",
"être exclues pour ,pre,poss",
"être exclus pour ,pre,poss",
"être écarté pour ,pre,poss",
"être écartée pour ,pre,poss",
"être écartés pour ,pre,poss",
"être écartées pour ,pre,poss",
"être éliminé pour ,pre,poss",
"être éliminée pour ,pre,poss",
"être éliminés pour ,pre,poss",
"être éliminées pour ,pre,poss",
"car,termin,hypo",
"parce que,termin,hypo",
"car,termin,neg",
"parce que,termin,neg",
"du fait que,termin,hypo",
"à cause de,termin,hypo",
"à cause d,termin,hypo",
"étant exclue ,post,poss",
"étant exclu ,post,poss",
"étant exclues ,post,poss",
"étant exclus ,post,poss",
"étant écarté ,post,poss",
"étant écartée ,post,poss",
"étant écartés ,post,poss",
"étant écartées ,post,poss",
"étant éliminé ,post,poss",
"étant éliminée ,post,poss",
"étant éliminés ,post,poss",
"étant éliminées ,post,poss",
"frère,pre,exp",
"frères,pre,exp",
"frere,pre,exp",
"freres,pre,exp",
"cousin,pre,exp",
"cousins,pre,exp",
"peut être écarté ,post,poss",
"peut être exclu ,post,poss",
"peut être exclue ,post,poss",
"peut être exclues ,post,poss",
"peut être exclus ,post,poss",
"peut être écartée ,post,poss",
"peut être écartés ,post,poss",
"peut être écartées ,post,poss",
"peut être éliminé ,post,poss",
"peut être éliminée ,post,poss",
"peut être éliminés ,post,poss",
"peut être éliminées ,post,poss",
"peut être écarté pour ,post,poss",
"peut être éliminé pour ,post,poss",
"peut être exclu pour ,post,poss",
"peut être écartée pour ,post,poss",
"peut être éliminée pour ,post,poss",
"peut être exclue pour ,post,poss",
"peuvent être écartés pour ,post,poss",
"peuvent être éliminés pour ,post,poss",
"peuvent être exclus pour ,post,poss",
"peuvent être écartées pour ,post,poss",
"peuvent être éliminées pour ,post,poss",
"peuvent être exclues pour ,post,poss",
"peut l'exclure ,post,neg",
"peut l'éliminer ,post,neg",
"peut l'écarter ,post,neg",
"peuvent l'exclure ,post,neg",
"peuvent l'éliminer ,post,neg",
"peuvent l'écarter ,post,neg",
"peut l'exclure contre ,post,neg",
"peut l'éliminer contre ,post,neg",
"peut l'écarter contre ,post,neg",
"peuvent l'exclure contre ,post,neg",
"peuvent l'éliminer contre ,post,neg",
"peuvent l'écarter contre ,post,neg",
"peut exclure ,pre,neg",
"peut éliminer ,pre,neg",
"peut écarter ,pre,neg",
"peuvent exclure ,pre,neg",
"peuvent éliminer ,pre,neg",
"peuvent écarter ,pre,neg",
"ne peut pas ,pre,neg",
"ne pouvons pas ,pre,neg",
"ne peuvent pas ,pre,neg",
"cause pour ,termin,neg",
"cause de ,termin,neg",
"cause d',termin,neg",
"causes pour ,termin,neg",
"causes de ,termin,neg",
"causes d',termin,neg",
"libre de,pre,neg",
"revenir pour,pre,hypo",
"revenir à,pre,hypo",
"se plaint,termin,histexp",
"pourrait être écarté ,post,poss",
"pourrait être écartée ,post,poss",
"pourraient être écartés ,post,poss",
"pourraient être écartées ,post,poss",
"pourrait être éliminé ,post,poss",
"pourrait être éliminée ,post,poss",
"pourraient être éliminés ,post,poss",
"pourraient être éliminées ,post,poss",
"pourrait être exclu ,post,poss",
"pourrait être exclue ,post,poss",
"pourraient être exclus ,post,poss",
"pourraient être exclues ,post,poss",
"pourrait être écarté pour ,pre,poss",
"pourrait être écartée pour ,pre,poss",
"pourraient être écartés pour ,pre,poss",
"pourraient être écartées pour ,pre,poss",
"pourrait être éliminé pour ,pre,poss",
"pourrait être éliminée pour ,pre,poss",
"pourraient être éliminés pour ,pre,poss",
"pourraient être éliminées pour ,pre,poss",
"pourrait être exclu pour ,pre,poss",
"pourrait être exclue pour ,pre,poss",
"pourraient être exclus pour ,pre,poss",
"pourraient être exclues pour ,pre,poss",
"actuellement,termin,histexp",
"en ce moment,termin,histexp",
"maintenant,termin,histexp",
"papa,pre,exp",
"refusé ,pre,neg",
"décliné ,pre,neg",
"décline ,pre,neg",
"refuse ,pre,neg",
"nié ,pre,neg",
"nie ,pre,neg",
"a nié ,pre,neg",
"nier ,pre,neg",
"niant ,pre,neg",
"n'a pas exclu ,post,poss",
"n'a pas écarté ,post,poss",
"n'a pas éliminé ,post,poss",
"l'a exclue ,pre,neg",
"l'a exclu ,pre,neg",
"l'a écarté ,pre,neg",
"l'a écartée ,pre,neg",
"l'a éliminé ,pre,neg",
"l'a éliminée ,pre,neg",
"a exclu ,pre,neg",
"a écarté ,pre,neg",
"a éliminé ,pre,neg",
"a exclu le patient ,pre,neg",
"a éliminé le patient ,pre,neg",
"a écarté le patient ,pre,neg",
"ne ressemble pas,pre,neg",
"ne ressemblent pas,pre,neg",
"ed,termin,hist",
"département d'urgence,termin,hist",
"service d'urgence,termin,hist",
"département d'urgences,termin,hist",
"service d'urgences,termin,hist",
"urgences,termin,hist",
"étiologie pour ,termin,neg",
"étiologie de ,termin,neg",
"étiologie d',termin,neg",
"etiologie de ,termin,neg",
"etiologie d',termin,neg",
"évaluer pour ,pre,neg",
"à l'exception de ,termin,neg",
"à l'exception d',termin,neg",
"ne révèle pas ,pre,neg",
"ne révèlent pas ,pre,neg",
"ne parvient pas à révéler ,pre,neg",
"jamais présenté ,pre,neg",
"ne voit pas ,pre,neg",
"famille,pre,exp",
"familial,pre,exp",
"familiaux,pre,exp",
"familiale,pre,exp",
"familiales,pre,exp",
"fam hx,pre,exp",
"père,pre,exp",
"paternel,pre,exp",
"pères,pre,exp",
"ne contient pas ,pre,neg",
"ne contiennent pas ,pre,neg",
"sans ,pre,neg",
"libre de ,pre,neg",
"grand-père,pre,exp",
"grand père,pre,exp",
"grand pere,pre,exp",
"pere,pre,exp",
"grands-pères,pre,exp",
"grand-mère,pre,exp",
"grand mère,pre,exp",
"grand mere,pre,exp",
"mere,pre,exp",
"neveu,pre,exp",
"nièce,pre,exp",
"niece,pre,exp",
"grand parent,pre,exp",
"grands parents,pre,exp",
"a été négatif,post,neg",
"a été négative,post,neg",
"a été écartée ,post,neg",
"a été infirmée ,post,neg",
"a été exclue ,post,neg",
"a été éliminée ,post,neg",
"a été écarté ,post,neg",
"a été infirmé ,post,neg",
"a été exclu ,post,neg",
"a été éliminé ,post,neg",
"ont été écartées ,post,neg",
"ont été infirmées ,post,neg",
"ont été écartés ,post,neg",
"ont été infirmés ,post,neg",
"ont été éliminés ,post,neg",
"ont été éliminées ,post,neg",
"ont été exclus ,post,neg",
"ont été exclues ,post,neg",
"sa,termin,hypoexp",
"son,termin,hypoexp",
"histoire,pre,hist",
"antécédents,pre,hist",
"antécédent,pre,hist",
"pas d'antécédent,pseudo,hist",
"pas d'antécédents,pseudo,hist",
"absence d'antécédent,pseudo,hist",
"absence d'antécédents,pseudo,hist",
"toutefois ,termin,neg",
"cependant ,termin,neg",
"pourtant ,termin,neg",
"si,pre,hypo",
"à condition,pre,hypo",
"si négatif,pseudo,hypo",
"si négative,pseudo,hypo",
"incompatible avec,pre,neg",
"incohérent avec,pre,neg",
"en contradiction avec,pre,neg",
"n'est pas,pre,neg",
"est exclue ,post,neg",
"est écarté ,post,neg",
"est infirmé ,post,neg",
"est écartée ,post,neg",
"est infirmée ,post,neg",
"est éliminé ,post,neg",
"est éliminés ,post,neg",
"est exclu ,post,neg",
"est à exclure ,post,poss",
"est à écarter ,post,poss",
"est à éliminer ,post,poss",
"manque de,pre,neg",
"absence de,pre,neg",
"absence d',pre,neg",
"manquait,pre,neg",
"maman,pre,exp",
"mère,pre,exp",
"maternel,pre,exp",
"négatif pour ,pre,neg",
"négative pour ,pre,neg",
"jamais développé ,pre,neg",
"pas développé ,pre,neg",
"jamais eu ,pre,neg",
"pas eu ,pre,neg",
"néanmoins ,termin,neg",
"non ,pre,neg",
"aucun ,pre,neg",
"aucune ,pre,neg",
"pas de ,pre,neg",
"pas d',pre,neg",
"pas de cause de ,pre,neg",
"aucune cause de ,pre,neg",
"pas de changement ,pseudo,neg",
"aucune plainte de ,pre,neg",
"pas de plainte de ,pre,neg",
"aucune preuve ,pre,neg",
"pas de preuve ,pre,neg",
"pas de preuves ,pre,neg",
"aucune constatation de ,pre,neg",
"aucune constatation pour indiquer ,pre,neg",
"pas d'augmentation ,pseudo,neg",
"n'est plus présent,post,neg",
"n'est plus visible,post,neg",
"n'est pas présent,post,neg",
"n'est pas visible,post,neg",
"aucune preuve mammographique de ,pre,neg",
"pas de nouveau ,pre,neg",
"aucun nouvel ,pre,neg",
"aucune nouvelle ,pre,neg",
"aucune autre preuve ,pre,neg",
"aucun autre signe ,pre,neg",
"aucune preuve radiographique de ,pre,neg",
"aucun signe ,pre,neg",
"aucun signe de ,pre,neg",
"non significatif ,post,neg",
"non significative ,post,neg",
"pas significatif ,post,neg",
"pas significative ,post,neg",
"pas de suggestion de ,pre,neg",
"aucun changement suspect ,pseudo,neg",
"pas de changements ,pseudo,neg",
"non diagnostique,post,neg",
"pas été diagnostiqué,post,neg",
"pas été diagnostiquée,post,neg",
"pas été diagnostiqués,post,neg",
"pas été diagnostiquées,post,neg",
"ne pas ,pre,neg",
"pas ,pre,neg",
"nullement ,pre,neg",
"ne pas apparaître ,pre,neg",
"pas apparu ,pre,neg",
"pas apparue ,pre,neg",
"pas apparus ,pre,neg",
"pas apparues ,pre,neg",
"ne pas apprécier ,pre,neg",
"non associé à ,pre,neg",
"non associée à ,pre,neg",
"non associés à ,pre,neg",
"non associées à ,pre,neg",
"non associé avec ,pre,neg",
"non associée avec ,pre,neg",
"non associés avec ,pre,neg",
"non associées avec ,pre,neg",
"pas associé à ,pre,neg",
"pas associée à ,pre,neg",
"pas associés à ,pre,neg",
"pas associées à ,pre,neg",
"pas associé avec ,pre,neg",
"pas associée avec ,pre,neg",
"pas associés avec ,pre,neg",
"pas associées avec ,pre,neg",
"pas été exclue ,post,poss",
"pas été exclu ,post,poss",
"pas été exclus ,post,poss",
"pas été exclues ,post,poss",
"pas certain si ,pseudo,neg",
"ne pas se plaindre ,pre,neg",
"pas se plaindre ,pre,neg",
"se plaint pas ,pre,neg",
"ne pas démontrer ,pre,neg",
"ne démontre pas ,pre,neg",
"ne manifeste pas ,pre,neg",
"ne présente pas ,pre,neg",
"n'avait pas ,pre,neg",
"ne démontrent pas ,pre,neg",
"ne pas ressentir ,pre,neg",
"ne ressent pas ,pre,neg",
"ne pas avoir ,pre,neg",
"n'a pas ,pre,neg",
"pas de preuves de,pre,neg",
"pas de preuve de,pre,neg",
"pas de signe de,pre,neg",
"pas de signe d',pre,neg",
"pas de risque de,pre,neg",
"pas de risque d',pre,neg",
"pas de signes de,pre,neg",
"pas de signes d',pre,neg",
"pas de risques de,pre,neg",
"pas de risques d',pre,neg",
"ne sais pas ,pre,neg",
"pas connu pour avoir ,pre,neg",
"pas connue pour avoir ,pre,neg",
"pas connus pour avoir ,pre,neg",
"pas connues pour avoir ,pre,neg",
"pas nécessairement ,pseudo,neg",
"pas sur,pseudo,neg",
"pas certain,pseudo,neg",
"pas seulement ,pseudo,neg",
"non seulement ,pseudo,neg",
"ne pas révéler ,pre,neg",
"révéle pas ,pre,neg",
"révélent pas ,pre,neg",
"montre pas ,pre,neg",
"montrent pas ,pre,neg",
"pas exclue ,post,poss",
"pas exclu ,post,poss",
"pas exclus ,post,poss",
"pas exclues ,post,poss",
"pas écarté ,post,poss",
"pas écartée ,post,poss",
"pas écartées ,post,poss",
"pas écartés ,post,poss",
"pas éliminé ,post,poss",
"pas éliminés ,post,poss",
"pas éliminée ,post,poss",
"pas éliminées ,post,poss",
"pas vu ,pre,neg",
"ne pas être ,pre,neg",
"pas être ,pre,neg",
" noté,termin,histexp",
"maintenant résolu,post,neg",
"maintenant résolue,post,neg",
"maintenant résolus,post,neg",
"maintenant résolues,post,neg",
"origine pour ,termin,neg",
"origine de ,termin,neg",
"origines pour ,termin,neg",
"origines de ,termin,neg",
"origines d',termin,neg",
"autres possibilités de ,termin,neg",
"autres possibilités d',termin,neg",
"antécédents médicaux,pre,hist",
"antécédent médical,pre,hist",
"patient,termin,hypoexp",
"patient n'était pas ,pre,neg",
"aucuns ,pre,neg",
"aucunes ,pre,neg",
"oriente pas vers ,pre,neg",
"orientent pas vers ,pre,neg",
"sans indice ,pre,neg",
"pas d'indice ,pre,neg",
"pas d'indices ,pre,neg",
"pas de nouvel indice ,pre,neg",
"pas de nouveaux indices ,pre,neg",
"pas de nouvelle preuve ,pre,neg",
"pas de nouvelles preuves ,pre,neg",
"pas d'autre preuve ,pre,neg",
"pas d'autre indice ,pre,neg",
"pas d'autres preuves ,pre,neg",
"pas d'autres indices ,pre,neg",
"pas d'argument en faveur de ,pre,neg",
"pas d'argument évident en faveur de ,pre,neg",
"pas le moindre ,pre,neg",
"pas la moindre ,pre,neg",
"ni de ,pre,neg",
"ni d',pre,neg",
"ni ,pre,neg",
"n'apparaît pas ,pre,neg",
"n'apparaîssent pas ,pre,neg",
"peu d'antécédent,pseudo,hist",
"peu d'antécédents,pseudo,hist",
"présentant,termin,histexp",
"présente,termin,histexp",
"précédent,one,hist",
"précédents,one,hist",
"prophylaxie,post,neg",
"prévenir,pre,neg",
"prévention,pre,neg",
"r/o,pre,poss",
"plutôt que ,pre,neg",
"raison pour ,termin,neg",
"raison de ,termin,neg",
"raison d',termin,neg",
"raisons pour ,termin,neg",
"raisons de ,termin,neg",
"raisons d',termin,neg",
"signalé,termin,histexp",
"signalée,termin,histexp",
"signalés,termin,histexp",
"signalées,termin,histexp",
"rapporté,termin,histexp",
"rapportée,termin,histexp",
"rapportés,termin,histexp",
"rapportées,termin,histexp",
"rapporte,termin,histexp",
"signale,termin,histexp",
"rapportent,termin,histexp",
"signalent,termin,histexp",
"résolu ,pre,neg",
"résoluent ,pre,neg",
"retourne,pre,hypo",
"l'éliminer ,pre,poss",
"l'exclure ,pre,poss",
"éliminer ,pre,poss",
"exclure ,pre,poss",
"écarter ,pre,neg",
"écarter le patient ,pre,poss",
"écarte ,pre,neg",
"écartent ,pre,neg",
"élimine ,pre,neg",
"éliminent ,pre,neg",
"excluent ,pre,neg",
"secondaire,termin,neg",
"auxiliaire,termin,neg",
"secondaires,termin,neg",
"auxiliaires,termin,neg",
"devrait-il,pre,hypo",
"devrait elle,pre,hypo",
"devrait-il y avoir,pre,hypo",
"depuis,termin,hypo",
"dès,termin,hypo",
"sœur,pre,exp",
"soeur,pre,exp",
"sœurs,pre,exp",
"soeurs,pre,exp",
"source pour ,termin,neg",
"source de ,termin,neg",
"source d',termin,neg",
"sources pour ,termin,neg",
"sources de ,termin,neg",
"sources d',termin,neg",
"stipule,termin,histexp",
"malgré ,termin,neg",
"exclure ,pre,neg",
"aujourd'hui,termin,histexp",
"événement déclencheur pour ,termin,neg",
"oncle,pre,exp",
"tonton,pre,exp",
"improbable ,post,neg",
"peu probable ,post,neg",
"pas remarquable pour ,pre,neg",
"a été trouvé,termin,histexp",
"a été trouvée,termin,histexp",
"était négatif,post,neg",
"étaient négatifs,post,neg",
"était négative,post,neg",
"étaient négatives,post,neg",
"n'était pas,pre,neg",
"n'étaient pas,pre,neg",
"lequel,termin,exp",
"que,termin,exp",
"qui,termin,hypoexp",
"sans pour autant ,pre,neg",
"sans aucune preuve de ,pre,neg",
"sans peine ,pseudo,neg",
"sans difficulté ,pseudo,neg",
"sans preuve ,pre,neg",
"sans preuves ,pre,neg",
"sans indication de ,pre,neg",
"sans indication d',pre,neg",
"sans signe ,pre,neg",
"éliminons ,pre,neg",
"écartons ,pre,neg",
"avait éliminé ,pre,neg",
"avons éliminé ,pre,neg",
"avions éliminé ,pre,neg",
"avait infirmé ,pre,neg",
"avons infirmé ,pre,neg",
"avions infirmé ,pre,neg",
"avait écarté ,pre,neg",
"avons écarté ,pre,neg",
"avions écarté ,pre,neg",
"avait exclu ,pre,neg",
"avons exclu ,pre,neg",
"avions exclu ,pre,neg",
"éliminons ,pre,neg",
"écartons ,pre,poss",
"excluons ,pre,poss",
"sont écartées ,post,neg",
"est éliminée ,post,neg",
"sont éliminés ,post,neg",
"sont éliminées ,post,neg",
"pas été écartée ,post,poss",
"pas été écartées ,post,poss",
"pas été écarté ,post,poss",
"pas été écartés ,post,poss",
"pas été éliminée ,post,poss",
"pas été éliminées ,post,poss",
"pas été éliminé ,post,poss",
"pas été éliminés ,post,poss",
"sera écartée ,post,poss",
"sera écarté ,post,poss",
"sera éliminée ,post,poss",
"sera éliminé ,post,poss",
"seront écartées ,post,poss",
"seront écartés ,post,poss",
"seront éliminées ,post,poss",
"seront éliminés ,post,poss",
"évènement déclencheur de ,termin,neg",
"évènement déclencheur d',termin,neg",
"origine d',termin,neg",
"origin de ,termin,neg",
"origin d',termin,neg",
"encore ,termin,neg",
"infirme ,pre,neg",
"infirment ,pre,neg",
"infirmer ,pre,neg"};
	
	private Pattern regexPseudo;
	
	private Pattern regexNegPre;
	private Pattern regexNegPost;
	private Pattern regexPossPre;
	private Pattern regexPossPost;
	private Pattern regexNegEnd;
	
	private Pattern regexExpPre;
	private Pattern regexExpEnd;
	
	private Pattern regexHypoPre;
	private Pattern regexHypoEnd;
	private Pattern regexHypoExpEnd;
	
	private Pattern regexHistPre;
	private Pattern regexHist1w;
	private Pattern regexHistEnd;
	private Pattern regexHistExpEnd;
	
	private Pattern regexTime;
	private Pattern regexTimeFor;
	private Pattern regexTimeSince;
	
	//originally this pattern recognized UMLS concepts, but for this application
	//it will recognize the input concepts
	private static final String regExUmlsTag = "\\[\\d+\\]"; 

	
	/**
	 * Initialization regex (load parameters)
	 * @throws IOException 
	 */
	public ConTextFrench()
	{	
		
		String regex_PSEUDO = "";
		String regex_NEG_PRE = "";
		String regex_NEG_POST = "";
		String regex_POSS_PRE = "";
		String regex_POSS_POST = "";
		String regex_NEG_END = "";
		
		String regex_EXP_PRE = "";
		String regex_EXP_END = "";
		
		String regex_HYPO_PRE = "";
		String regex_HIST_PRE = "";
		String regex_HIST_1W = "";
		
		String regex_HYPO_END = "";
		String regex_HIST_END = "";
		String regex_HIST_EXP_END = "";
		String regex_HYPO_EXP_END = "";
		
		for (int i=0; i < regexes.length; i++)
		{
			int attrIndex = regexes[i].indexOf(',');
			int attrIndex2 = regexes[i].lastIndexOf(',');
			
			String phrase = regexes[i].substring(0,attrIndex).replaceAll(" ", "[\\\\s\\\\-]");
			String position = regexes[i].substring(attrIndex+1, attrIndex2);
			String contextType = regexes[i].substring(attrIndex2+1);
			
			if (position.compareTo("pseudo")==0)
			{
				regex_PSEUDO = regex_PSEUDO + "|" + phrase;
			}
			else if (position.compareTo("termin")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_END = regex_NEG_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypo")==0)
					regex_HYPO_END = regex_HYPO_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_END = regex_HIST_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("histexp")==0)
					regex_HIST_EXP_END = regex_HIST_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypoexp")==0)
					regex_HYPO_EXP_END = regex_HYPO_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("exp")==0)
					regex_EXP_END = regex_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			else if (position.compareTo("pre")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_PRE = regex_NEG_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("poss")==0)
					regex_POSS_PRE = regex_POSS_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypo")==0)
					regex_HYPO_PRE = regex_HYPO_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("exp")==0)
					regex_EXP_PRE = regex_EXP_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_PRE = regex_HIST_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_1W = regex_HIST_1W + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			else if (position.compareTo("post")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_POST = regex_NEG_POST + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("poss")==0)
					regex_POSS_POST = regex_POSS_POST + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			
		}
		if (regex_PSEUDO.length()>0)
			regexPseudo = Pattern.compile(regex_PSEUDO.substring(2));
		
		//negation context
		if (regex_NEG_PRE.length()>0)
			regexNegPre = Pattern.compile(regex_NEG_PRE.substring(1));
		if (regex_NEG_POST.length()>0)
			regexNegPost = Pattern.compile(regex_NEG_POST.substring(1));
		if (regex_NEG_END.length()>0)
			regexNegEnd = Pattern.compile(regex_NEG_END.substring(1));
		if (regex_POSS_PRE.length()>0)
			regexPossPre = Pattern.compile(regex_POSS_PRE.substring(1));
		if (regex_POSS_POST.length()>0)
			regexPossPost = Pattern.compile(regex_POSS_POST.substring(1));
		
		//temporality context
		if (regex_HIST_PRE.length()>0)
			regexHistPre = Pattern.compile(regex_HIST_PRE.substring(1));
		if (regex_HYPO_PRE.length()>0)
			regexHypoPre = Pattern.compile(regex_HYPO_PRE.substring(1));
		if (regex_HIST_1W.length()>0)
			regexHist1w = Pattern.compile(regex_HIST_1W.substring(1));
		if (regex_HIST_END.length()>0)
			regexHistEnd = Pattern.compile(regex_HIST_END.substring(1));
		if (regex_HYPO_END.length()>0)
			regexHypoEnd = Pattern.compile(regex_HYPO_END.substring(1));
		
		//experiencer and mixed
		if (regex_EXP_PRE.length()>0)
			regexExpPre = Pattern.compile(regex_EXP_PRE.substring(1));
		if (regex_EXP_END.length()>0)
			regexExpEnd = Pattern.compile(regex_EXP_END.substring(1));
		if (regex_HYPO_EXP_END.length()>0)
			regexHypoExpEnd = Pattern.compile(regex_HYPO_EXP_END.substring(1));
		if (regex_HIST_EXP_END.length()>0)
			regexHistExpEnd = Pattern.compile(regex_HIST_EXP_END.substring(1));
		
		
		/**********
		System.out.println("-----------------------------------------------------");
		System.out.println("NEGEX CONFIG: ");
		System.out.println("\tMax window: " + MAX_WINDOW + " words");
		System.out.println("\tPRENEG:   " + regex_NEG_PRE.substring(1));
		System.out.println("\tPOSTNEG:  " + regex_NEG_POST.substring(1));
		System.out.println("\tPREPOSS:  " + regex_POSS_PRE.substring(1));
		//System.out.println("POSTPOSS: " + regex_POSS_POST.substring(1));
		System.out.println("\tPSEUDO:   " + regex_PSEUDO.substring(1));
		System.out.println("\tNEGTERM:  " + regex_NEG_END.substring(1));
		System.out.println("\n");
		**********/
	}
	
	/**
	 * Pre-processing on the sentence (replace concepts and negation terms by keywords)
	 * @param sent
	 * @return Tagged sentence (concepts and context base terms)
	 * @throws Exception 
	 */
	@Override
	public String preProcessSentence(String sent, String concept) throws Exception
	{
		String sentenceTagged = " " + sent.replaceAll("\\s+", " ").toLowerCase();
		
		int lastOffset = 0;
		int charOffset=0;
		
		String tag="";
		String umlsConcept = concept.replaceAll("\\s+", " ").toLowerCase();
		tag = " [0] ";
		
		int conceptIndex = sentenceTagged.indexOf(umlsConcept);
		if (conceptIndex != -1)
		{
			charOffset = conceptIndex;
			sentenceTagged = sentenceTagged.substring(0,charOffset) + tag + sentenceTagged.substring(charOffset+umlsConcept.length());
			lastOffset = charOffset + tag.length();
		}
		else
			return null;
		
		
		//replacing negation phrases with corresponding tags
		
		//negation phrases
		if (regexPseudo != null){
			Matcher m0 = regexPseudo.matcher(sentenceTagged);
			sentenceTagged = m0.replaceAll(" <NEG_PSEUDO> ");
		}
		if (regexNegPre != null){
			Matcher m1 = regexNegPre.matcher(sentenceTagged);
			sentenceTagged = m1.replaceAll(" <NEG_PRE> ");
		}
		if (regexPossPre != null){
			Matcher m2 = regexPossPre.matcher(sentenceTagged);
			sentenceTagged = m2.replaceAll(" <POSS_PRE> ");
		}
		if (regexNegPost != null){
			Matcher m3 = regexNegPost.matcher(sentenceTagged);
			sentenceTagged = m3.replaceAll(" <NEG_POST> ");
		}
		if (regexPossPost != null){
			Matcher m4 = regexPossPost.matcher(sentenceTagged);
			sentenceTagged = m4.replaceAll(" <POSS_POST> ");
		}
		if (regexNegEnd != null){
			Matcher m5 = regexNegEnd.matcher(sentenceTagged);
			sentenceTagged = m5.replaceAll(" <NEG_END> ");
		}
		
		//experiencer phrases
		if (regexExpPre != null){
			Matcher m6 = regexExpPre.matcher(sentenceTagged);
			sentenceTagged = m6.replaceAll(" <EXP_PRE> ");
		}
		if (regexExpEnd != null){
			Matcher m14 = regexExpEnd.matcher(sentenceTagged);
			sentenceTagged = m14.replaceAll(" <EXP_END> ");
		}
		
		//hypothesis
		if (regexHypoPre != null){
			Matcher m7 = regexHypoPre.matcher(sentenceTagged);
			sentenceTagged = m7.replaceAll(" <HYPO_PRE> ");
		}
		if (regexHypoEnd != null){
			Matcher m10 = regexHypoEnd.matcher(sentenceTagged);
			sentenceTagged = m10.replaceAll(" <HYPO_END> ");
		}
		
		//temporality
		if (regexHistPre != null){
			Matcher m8 = regexHistPre.matcher(sentenceTagged);
			sentenceTagged = m8.replaceAll(" <HIST_PRE> ");
		}
		if (regexHist1w != null){
			Matcher m9 = regexHist1w.matcher(sentenceTagged);
			sentenceTagged = m9.replaceAll(" <HIST_1W> ");
		}
		if (regexHistEnd != null){
			Matcher m12 = regexHistEnd.matcher(sentenceTagged);
			sentenceTagged = m12.replaceAll(" <HIST_END> ");
		}
		
		// mixed
		if (regexHypoExpEnd != null){
			Matcher m11 = regexHypoExpEnd.matcher(sentenceTagged);
			sentenceTagged = m11.replaceAll(" <HYPO_EXP_END> ");
		}
		if (regexHistExpEnd != null){
			Matcher m13 = regexHistExpEnd.matcher(sentenceTagged);
			sentenceTagged = m13.replaceAll(" <HIST_EXP_END> ");
		}
		
		//time 
		regexTime = Pattern.compile("((1[4-9]|[1-9]?[2-9][0-9])[ |-][jour|jours] de)|" +
				"(([2-9]|[1-9][0-9])[ |-][semaine|semaines] de)|" +
				"(([1-9]?[0-9])[ |-][mois|an|ans|année|années] de)");//pattern to recognize expressions of >14 days
		regexTimeFor = Pattern.compile("[pour|pendant] [le|la|les] [dernier|dernière|dernières|derniers] (((1[4-9]|[1-9]?[2-9][0-9])[ |-][jour|jours] de)|" +
				"(([2-9]|[1-9][0-9])[ |-][semaine|semaines] de)|" +
				"(([1-9]?[0-9])[ |-][mois|an|ans|année|années] de))");//other pattern to recognize expressions of >14 days
		regexTimeSince = Pattern.compile("depuis [le dernier|la dernière|les derniers|les dernières]? ((([2-9]|[1-9][0-9]) semaines)|" +
				"(([1-9]?[0-9])? [mois|an|ans|année|années])|" +
				"([janvier|février|mars|avril|mai|juin|juillet|aout|août|septembre|octobre|novembre|décembre|decembre|printemps|été|hiver|automne]))");
		Matcher mTime = regexTimeFor.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_PRE> ");
		mTime = regexTime.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_PRE> ");
		mTime = regexTimeSince.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_POST> ");
		
		return sentenceTagged;
	}
	
	/**
	 * Context analysis on the given sentence.
	 * @param concept concept in the sentence
	 * @param sentence Sentence to analyze
	 */
	@Override
	public ArrayList<String> applyContext(String concept, String sentence) throws Exception
	{
		if(concept.equals("") || sentence.equals(""))
			return null;
		
		String tagged = preProcessSentence(sentence, concept);
		
		if(tagged==null)
			return null;
		
		//tokenizing the sentence in words
		String[] words =  tagged.split("[,;\\s]+");
		
		String ne = applyNegEx(words);
		String tmp = applyTemporality(words);
		String subj = applyExperiencer(words);
		
		ArrayList result = new ArrayList();
		result.add(concept); result.add(sentence);
		result.add(ne);result.add(tmp);result.add(subj);
		
		return result;
	}
	
	/**
	 * Apply NegEx algorithm to find negation context of the concepts found in the sentence
	 * @return
	 */
	@Override
	public String applyNegEx(String[] words) throws Exception
	{
		//Going from one negation to another, and creating the appropriate window
		int m = 0;
		List<String> window = new ArrayList<String>();
		
		//for each word in the sentence
		while (m < words.length)
		{
			//IF word is a pseudo-negation, skips to the next word
			if(words[m].equals("<NEG_PSEUDO>"))
			{
				m++;
			}
			//IF word is a pre- concept negation or possible...
			else if(words[m].matches("<NEG_PRE>|<PREP>"))
			{
				//find window (default is six words after the negation phrase)
				int maxWindow = MAX_WINDOW;
				if (words.length < m + maxWindow) maxWindow = words.length - m;
				for(int o=1; o < maxWindow; o++)
				{
					if(words[m+o].matches("<NEG_PRE>|<PREP>|<NEG_POST>|<POSS_POST>|<NEG_END>"))
						break;
					else window.add(words[m+o]);
				}
				
				//get type of Negation
				NegationContext currentNegationContext = NegationContext.Affirmed;
				if (words[m].equals("<NEG_PRE>")) {
					currentNegationContext = NegationContext.Negated;
				}
				else if(words[m].equals("<POSS_PRE>")) 
					currentNegationContext = NegationContext.Possible;
				
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setNegationContext(currentNegationContext.name());
						return currentNegationContext.name();
					}
				}
				window.clear();
				m++;
			}
			//IF word a post- concept negation or possible
			else if(words[m].matches("<NEG_POST>|<POSS_POST>"))
			{
				//find window (default is six words before the negation phrase)
				int maxWindow = MAX_WINDOW;
				if (m < maxWindow) maxWindow = m;
				for(int o=1; o < maxWindow; o++) {
					if(words[m-o].matches("<NEG_PRE>|<POSS_PRE>|<NEG_POST>|<POSS_POST>|<NEG_END>"))
						break;
					else
						window.add(words[m-o]);
				}
				
				//get type of Negation
				NegationContext currentNegationContext = NegationContext.Affirmed;
				if (words[m].equals("<NEG_POST>")){
					currentNegationContext = NegationContext.Negated;
				}
				else if(words[m].equals("<POSS_POST>")) 
					currentNegationContext = NegationContext.Possible;
					
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setNegationContext(currentNegationContext.name());
						return currentNegationContext.name();
					}
				}
				window.clear();
				m++;
			}
			//IF word not a negation or conjunction skip
			else{
				m++;
			}
		}
		return NegationContext.Affirmed.name();
	}
	
	/**
	 * Temporality analysis
	 * @return
	 */
	@Override
	public String applyTemporality(String[] words) throws Exception
	{

		List<String> window = new ArrayList<String>();
		
		
	
		//Going from one temporality term to another, and creating the appropriate window
		int mm = 0;
		while(mm<words.length)
		{
			//IF word is a pseudo-negation, skips to the next word
			if(words[mm].equals("<NEG_PSEUDO>")) mm++;
	
			//IF word is a pre- hypothetical trigger term
			else if(words[mm].equals("<HYPO_PRE>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].equals("<HYPO_END>|<HYPO_EXP_END>|<HYPO_PRE>")) {
						break;//window decreased to right before other negation or conjunction
					}
					else 
						window.add(words[mm+o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Hypothetical.name());
						return TemporalityContext.Hypothetical.name();
					}
				}
				window.clear();
				mm++;
			}
			//IF word a pre- historical trigger term
			else if(words[mm].matches("<HIST_PRE>|<TIME_PRE>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].matches("<HIST_END>|<HIST_EXP_END>|<HIST_PRE>|<HIST_1W>")) {
						break;//window decreased to right after other negation or conjunction
					}
					else window.add(words[mm+o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Historical.name());
						return TemporalityContext.Historical.name();
					}
				}
				window.clear();
				mm++;
			}
			//IF word a post- historical trigger term
			else if(words[mm].equals("<TIME_POST>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm-o)>=0; o++) {
					if(words[mm-o].matches("<HIST_END>|<HIST_EXP_END>|<HIST_PRE>|<HIST_1W>")) {
						break;//window decreased to right after other negation or conjunction
					}
					else window.add(words[mm-o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Historical.name());
						return TemporalityContext.Historical.name();
					}
				}
				window.clear();
				mm++;
			}
			else mm++;
		}
		return TemporalityContext.Recent.name();
	}
	
	/**
	 * Experiencer analysis
	 * @return
	 */
	@Override
	public String applyExperiencer(String[] words) throws Exception
	{
		List<String> window = new ArrayList<String>();
		
		//Going from one experiencer term to another, and creating the appropriate window
		int mm = 0;
		while(mm<words.length){
			//IF word is a pseudo-negation, skips to the next word
			if(words[mm].equals("<NEG_PSEUDO>")) mm++;
	
			//IF word is a pre- experiencer trigger term
			else if(words[mm].equals("<EXP_PRE>"))
			{
				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].equals("<EXP_END>|<HIST_EXP_END>|<HYPO_EXP_END>|<EXP_PRE>")) {
						break;//window decreased to right before other negation or conjunction
					}
					else window.add(words[mm+o]);
				}
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w); 
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setIsExperiencer(false);
						return "Other";
					}
				}
				window.clear();
				mm++;
			}
			else mm++;
		}
		return "Patient";
	}
	


}
