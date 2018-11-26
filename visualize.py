# -*- coding: utf-8 -*-

import csv
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

A0 = np.loadtxt(open("/home/tj/Documents/IntelliSys/A0.csv"), delimiter=",")
B0 = np.loadtxt(open("/home/tj/Documents/IntelliSys/B0.csv"), delimiter=",")

dfB = pd.read_csv("/home/tj/Documents/IntelliSys/B0.csv")
dfA =  pd.read_csv("/home/tj/Documents/IntelliSys/A0.csv")

dfDATA =  pd.read_csv("/home/tj/Documents/IntelliSys/data.csv")


fig = plt.figure()
ax1 = fig.add_subplot(111, projection='3d')
x = dfDATA.shape[0]
y = dfDATA.shape[1]
z = np.array(dfDATA)


#ax1.plot_surface(x/len(dfDATA),y/len(dfDATA),dfDATA)
#plt.show()

#-------------
Data1D = np.loadtxt("/home/tj/Documents/IntelliSys/data.csv", delimiter=",")

X,Y = np.meshgrid(Data1D[:,0], Data1D[:,1])
Z=np.tile(Data1D[:,2], (len(Data1D[:,2]),1))

fig1 = plt.figure()
ax = fig1.add_subplot(111, projection='3d')
ax.plot_surface(X,Y,Z, cmap='ocean')
plt.show()