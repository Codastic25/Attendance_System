import json
import os
import shutil

# Chemin du fichier JSON
names_file = '/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/names.json'

# Chemin du dossier Dataset
dataset_folder = '/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/Dataset'

# Vider le fichier JSON en enregistrant une liste vide
with open(names_file, 'w') as file:
    json.dump([], file)

print("La liste dans le fichier 'names.json' a été vidée.")

# Supprimer tout le contenu du dossier Dataset
if os.path.exists(dataset_folder):
    # Parcourir tous les fichiers et sous-dossiers du dossier
    for filename in os.listdir(dataset_folder):
        file_path = os.path.join(dataset_folder, filename)
        try:
            if os.path.isdir(file_path):
                shutil.rmtree(file_path)  # Supprimer un sous-dossier
            else:
                os.remove(file_path)  # Supprimer un fichier
        except Exception as e:
            print(f"Erreur lors de la suppression de {file_path}: {e}")

print(f"Le contenu du dossier '{dataset_folder}' a été supprimé.")
