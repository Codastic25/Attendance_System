import sys
import os
import json
import cv2

# Récupérer le nom passé en argument
if len(sys.argv) < 2:
    print("Erreur : Aucun nom fourni en argument.")
    sys.exit(1)

name = sys.argv[1]
print(f"Nom saisi : {name}")

# Chemin du fichier contenant les noms
names_file = '/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/names.json'

# Charger les noms existants
if os.path.exists(names_file):
    with open(names_file, 'r') as file:
        dataset = json.load(file)
else:
    dataset = []

# Ajouter le nom s'il n'existe pas déjà
if name not in dataset:
    dataset.append(name)
    with open(names_file, 'w') as file:
        json.dump(dataset, file)

# Créer un dossier pour l'employé dans le dossier Dataset
dataset_path = '/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/Dataset'
person_folder = os.path.join(dataset_path, name)

if not os.path.exists(person_folder):
    os.makedirs(person_folder)

# Initialisation de la caméra
cap = cv2.VideoCapture(0)

# Charge le classificateur de visages Haar Cascade
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

print("En attente de capture des photos... Appuyez sur 'q' pour quitter.")
photo_count = 0
max_photos = 40

while True:
    ret, frame = cap.read()
    if not ret:
        print("Impossible de capter l'image.")
        break

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        face = frame[y:y+h, x:x+w]
        photo_count += 1
        photo_filename = os.path.join(person_folder, f"{name}_{photo_count}.jpg")
        cv2.imwrite(photo_filename, face)
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

    cv2.imshow("Capture", frame)

    if cv2.waitKey(1) & 0xFF == ord('q') or photo_count >= max_photos:
        break

cap.release()
cv2.destroyAllWindows()

print(f"Capture terminée. {photo_count} photos ont été enregistrées dans {person_folder}.")
