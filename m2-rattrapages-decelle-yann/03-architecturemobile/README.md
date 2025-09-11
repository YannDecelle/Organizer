# 03 – Architecture Mobile

## Description

Mini-application Android développée en Kotlin mettant en œuvre l’architecture **MVVM** avec **Room** pour la gestion locale des données.
L’application permet de gérer une liste de tâches avec rappels et un historique.

---

## Installation & lancement

### 1. Cloner le projet

```bash
git clone https://github.com/ton-projet/03ArchitectureMobile.git
```

### 2. Ouvrir dans Android Studio

* Ouvrir Android Studio.
* Cliquer sur **Open an existing project** et sélectionner le dossier.
* Android Studio télécharge automatiquement les dépendances Gradle.

⚠️ **Note :** parfois Android Studio peut avoir du mal à détecter le Gradle.
Dans ce cas, une popup apparaît en bas à droite → cliquez dessus pour **réindiquer le chemin du fichier lorsque la popup apparait**.

### 3. Lancer l’application

* Brancher un téléphone Android (mode développeur activé) **ou** lancer un émulateur (Pixel, API 30+ recommandé).
* Cliquer sur **Run** dans Android Studio.
* L’application s’installe et démarre automatiquement.

### 4. Fonjctionnalité

Les fonctionnalités sont les suivantes : 

Création / Suppression / édition de tâche

Création / Suppression / édition de Rappels

Si une tâche a une deadline, cela créer automatiquement un rappel

Si la date du rappel est dépassé, il sera affiché dans l'historique

L'historique permet de supprimer tous les rappel dépassé à l'aide d'un bouton

## Démonstration

[Demo video](https://youtube.com/shorts/zJ8G9R0I6jw?si=e1b_Xi-Mt5LIX0SL)
