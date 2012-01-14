from Tkinter import *
import os
import tkFileDialog
import subprocess
import threading
import time

running = False

os.chdir("build")

class FileSelector:
    def __init__(self, master, typ):
        self.typ = typ
        self.frame = Frame(master)
        self.frame.pack(expand=1, fill=BOTH)
        self.entry = Entry(self.frame)
        self.entry.pack(expand=1, fill=X, side=LEFT)
        self.button = Button(self.frame, text="Open...", command=self.callback)
        self.button.pack(expand=1, fill=X, side=RIGHT)

    def callback(self, *args):
        if self.typ == "open":
            fname = tkFileDialog.askopenfilename(title="Please Choose A File...")
        elif self.typ == "save":
            fname = tkFileDialog.asksaveasfilename(title="Please Choose A File...")

        if fname:
            self.entry.delete(0, END)
            self.entry.insert(0, fname)

    def get(self):
        return self.entry.get()

    def pack(self, **kwargs):
        self.frame.pack(**kwargs)

class ThreadExec:
    def thread(self):
        self.lock.acquire()
        subprocess.call([self.prog] + list(self.args))
        self.lock.release()

    def __init__(self, prog, *args):
        self.lock = threading.Lock()
        self.prog = prog
        self.args = args
        logger.insert(END, time.strftime("[%H:%M:%S] Starting..."))
        threading.Thread(target=self.thread).start()

    def wait(self):
        if not self.lock.acquire(False):
            window.after(1000, self.wait)
        else:
            logger.insert(END, "Done\n")

def entry_label(master, labeltext):
    fr = Frame(master)
    fr.pack(expand=1, fill=BOTH)
    Label(fr, text=labeltext).pack(expand=1, fill=X, side=LEFT)
    e = Entry(fr)
    e.pack(expand=1, fill=X, side=RIGHT)
    return e

def send_file(*args):
    ip = sendip.get()
    port = sendpt.get()
    fname = sendfs.get()
    ThreadExec("java", "CLI", "send", ip, port, fname).wait()

def recv_file(*args):
    port = sendpt.get()
    fname = sendfs.get()
    ThreadExec("java", "CLI", "read", port, fname).wait()

window = Tk()

sendframe = Frame(window); sendframe.pack(expand=1, fill=BOTH)
readframe = Frame(window); readframe.pack(expand=1, fill=BOTH)

sendip = entry_label(sendframe, "IP Address"); sendip.pack(expand=1, fill=X)
sendpt = entry_label(sendframe, "Port"); sendpt.pack(expand=1, fill=X)
sendfs = FileSelector(sendframe, "open"); sendfs.pack(expand=1, fill=X)
sendbt = Button(sendframe, text="Send File", command=send_file); sendbt.pack(expand=1, fill=X)

readpt = entry_label(readframe, "Port"); readpt.pack(expand=1, fill=X)
readfs = FileSelector(readframe, "save"); readfs.pack(expand=1, fill=X)
readbt = Button(readframe, text="Read File", command=recv_file); readbt.pack(expand=1, fill=X)

logger = Text(window); logger.pack(expand=1, fill=X)

window.mainloop()
