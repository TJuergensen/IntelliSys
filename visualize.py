# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.mlab as mlab
import math

fig = plt.figure()
mu = 0
variance = 2
sigma=math.sqrt(variance)
threshold = (5.5,0.4)
x= np.linspace(mu -3 * sigma, mu +3*sigma, 100)
plt.plot(x+4, mlab.normpdf(x, mu,sigma))
plt.plot(x+7, mlab.normpdf(x, mu,sigma))

plt.axvline(x=7, color="red")

plt.show()