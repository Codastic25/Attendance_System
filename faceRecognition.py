import cv2
import numpy as np
import os
import json

# Chemin absolu du fichier de noms
names_file = '/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/names.json'

# Charger les noms depuis le fichier JSON
if os.path.exists(names_file):
    with open(names_file, 'r') as file:
        dataset = json.load(file)
else:
    print(f"Erreur : Le fichier '{names_file}' n'existe pas.")
    exit()

# Chemin absolu du répertoire Dataset
dataset_path = "/home/cytech/id3/java/projets/attendance_system/attendance_projet/src/Dataset"

# Vérifier si le répertoire "Dataset" existe
if not os.path.exists(dataset_path):
    print(f"Erreur : Le répertoire '{dataset_path}' n'existe pas.")
    exit()

# Initialiser le modèle de reconnaissance faciale
face_recognizer = cv2.face.LBPHFaceRecognizer_create()  # OpenCV 4.x

# Fonction pour détecter des visages
def detect_faces(img):
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    faceCasc = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_frontalface_default.xml")  # Utiliser le chemin complet
    faces = faceCasc.detectMultiScale(gray, 1.3, 5)
    graylist = []
    faceslist = []

    if len(faces) == 0:
        return None, None

    for i in range(0, len(faces)):
        (x, y, w, h) = faces[i]
        graylist.append(gray[y:y+h, x:x+w])  # Extraire le visage en niveaux de gris
        faceslist.append(faces[i])

    return graylist, faceslist

# Fonction d'entraînement des données
def data():
    dirs = os.listdir(dataset_path)
    faces = []
    labels = []

    label = 1  # Initialiser le label à 1 pour le premier utilisateur

    for i in dirs:
        person_folder = os.path.join(dataset_path, i)

        if i in dataset:
            # Si le nom du dossier est dans le dataset, on l'utilise comme label
            label = dataset.index(i)  # Trouver l'index dans le dataset
        else:
            # Si le nom du dossier n'est pas dans le dataset, passer au dossier suivant
            print(f"Dossier {i} ignoré car il n'est pas dans le dataset.")
            continue

        for j in os.listdir(person_folder):
            path = os.path.join(person_folder, j)
            img = cv2.imread(path)
            if img is None:
                print(f"Erreur : Impossible de lire l'image {path}")
                continue
            face, rect = detect_faces(img)

            if face is not None:
                # Redimensionner les visages à une taille standard
                for f in face:
                    resized_face = cv2.resize(f, (200, 200))  # Redimensionner le visage à une taille fixe (200x200)
                    faces.append(resized_face)
                    labels.append(label)

    return faces, labels

faces, labels = data()

# Entraîner le modèle si des données sont présentes
if faces and labels:
    # Convertir les données en tableau numpy (les visages doivent être des tableaux 2D)
    faces = [np.array(f, dtype=np.uint8) for f in faces]
    labels = np.array(labels)

    face_recognizer.train(faces, labels)
else:
    print("Erreur : Pas de données pour l'entraînement.")
    exit()


#fonction de prédiction
def predict(img, face_recognizer):
    face, rect = detect_faces(img)

    if face is not None:
        for i in range(0, len(face)):
            label, confidence = face_recognizer.predict(face[i])
            label_text = dataset[label].strip()  # Retirer les espaces inutiles

            (x, y, w, h) = rect[i]
            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0))
            cv2.putText(img, label_text, (x, y), cv2.FONT_HERSHEY_PLAIN, 1.5, (0, 255, 0), 2)

        return img, label_text.lower()  # Retourner le nom proprement
    return img, "unknown"  # Si aucun visage détecté



# Capture vidéo et prédiction en temps réel
video_capture = cv2.VideoCapture(0)

if not video_capture.isOpened():
    print("Erreur : impossible d'ouvrir la caméra.")
    exit()

while True:
    ret, frame = video_capture.read()

    if not ret:
        print("Erreur de capture, en attente d'une image valide...")
        continue

    frame, recognized_name = predict(frame, face_recognizer)

    # Afficher uniquement le nom reconnu
    print(recognized_name)

    # Afficher l'image avec la reconnaissance faciale
    cv2.imshow('Video', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video_capture.release()
cv2.destroyAllWindows()



# Appuyer sur 'q' pour quitter
