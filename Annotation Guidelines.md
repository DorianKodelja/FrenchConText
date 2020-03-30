# Annotation Guidelines

Created in 2017 for the development of a corpus for the evalaution of French ConText algorithm. 
Amine Abdaoui (@amineabdaoui)
Copied in March 2020 from https://github.com/amineabdaoui/FrenchConText/blob/master/Annotation%20Guidelines.md

#### Introduction 
The aim of this manual annotation process is to detect the context of pre-tagged medial conditions (disorders). In this process, we will **not** annotate medical conditions **nor** lexical cues that change the context of these conditions (for example 'no'). The manual annotation only consists in detecting the context (for example negated or affirmed) of each tagged entity (medical condition). The pre-tagged conditions have been already detected using the SIFR Annotator (bioportal.lirmm.fr/annotator).

To start annotating please follow the following steps.

## Installation Instructions
Since, the brat server assumes a UNIX-like environment, the following instructions should work on either linux or mac. If you are running windows, you should in a virtual machine running a UNIX-like operating system such as Ubuntu. Python should also be installed on your operating system, this is usually the case on UNIX-like environments.
1. Unpack the zip package "brat-v1.3.zip" transmitted to you
2. Access to the created folder on a terminal `cd brat-v1.3`
3. Run the installation script in “unprivileged” mode `./install.sh -u`
4. Choose the user name and password of your brat server (you can leave the email blank)
5. Run the installed brat server using the command `python standalone.py`

## Annotation Instructions
1. Access to the installed brat server on your web browser http://127.0.0.1:8001
2. Click Ok and select the folder ConText
3. Select the context you want to annotate (Negation, Temporality and Experiencer)
4. Inside the chosen folder, open a file to start annotating
5. Click on the brat icon in the top right corner of your window
6. Login using the username and password you have chosen in the previous step
7. For each tagged condition, if its context value is not the default one, double click on it and choose the appropriate contextual value
8. You can pause close the annotation window any time, you will be able to continue annotation later without loosing your work.

You have to login before starting the annotation, otherwise the conditions will not be clickable.

## Contextual values
For each contextual dimension, the following values have been considered. If you are uncertain about the context value of a specific condition please email me.

### Negation:

- **Default value:** Affirmed
- **Other Values:** Negated, Possible

### Temporality:

- **Default value:** Recent
- **Other Value:** Historical


### Experiencer:

- **Default value:** Patient
- **Other Value:** Other

## Additional details :

- A condition is considered historical if it occurred more than 2 weeks ago. For example the condition "_cardiac procedure_"  in the sentence "_The patient underwent a cardiac procedure 6 months ago_" is considered historical, while "_fever_" in "_The patient had fever last night_" is considered recent
- A hypothetical condition concerns the future (it may happen in the future). For example: "_Stop treatment immediately in case of allergy_"
- A condition receives the value other for the experiencer dimension if it does not concern the patient. For example: "_The patient's father died of cancer_"
- A condition takes a non-default contextual value only if an external term changed its default value. For example the term "_afebrile_" is affirmed in the following sentence: "_the patient is afebrile_" even if it means that the patient does not have fever.
