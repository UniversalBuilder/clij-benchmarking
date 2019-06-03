# Benchmarking CLIJ operations versus ImageJ/Fiji operatipns
In order to measure performance differences between ImageJ and CLIJ operations, we conducted benchmarking experiments.

## Benchmarking operations
First, we set up a list of operations to benchmark in more detail. The list contains typical operations such as
morpholigical filters, binary image operations, thresholding and projections. The operations were encapsulated in
respective Benchmarking modules and put in a [Java package](https://github.com/clij/clij-benchmarking/tree/master/src/main/java/net/haesleinhuepf/clij/benchmark/modules);
Each of these methods has two test methods: One that processes images in an ImageJ-way and another that executes an 
as-similar-as-possible operation using CLIJ. Both methods measure the pure processing time excluding time spent for
data transfer between CPU and GPU for example. An exceptional module called [Transfer3DFrom](https://github.com/clij/clij-benchmarking/blob/master/src/main/java/net/haesleinhuepf/clij/benchmark/modules/Transfer3DFrom.java)
is used to quantify the transfer time exclusively.

## Image data
For benchmarking operations, we used images with random pixel values of pixel type 8-bit with a size of 16 MB if not specified otherwise. 
The 2D-operations were tested on images of size 4096 x 4096. The 3D-operations received 256 x 256 x 256 large image stacks for processing.

## Benchmarked computing hardware
Benchmarking was executed on 
* a mid-range priced laptop with an Intel Core i7-8650U CPU and a Intel UHD 620 GPU, 
* a workstation with an Intel Xeon Silver 4110 CPU in combination with a Nvidia Quadro P6000 GPU. 

For the speedup calculation the CPU of the notebook executing the ImageJ workflow version was used as reference.

## Results

### Transfer time
The measured median transfer times +- standard deviations show clear differences between notebook 
(push: 0.5 +- 0.4 GB/s, pull 0.4 +- 0.5 GB/s) and workstation (push: 1.3 +- 0.6 GB/s, pull: 1.3 +- 0.4 GB/s ) 
suggesting that the data transfer is faster on the workstation.
These numbers can be retraced by executing the [analyse_transfer_time.py](https://github.com/clij/clij-benchmarking/tree/master/plotting/python/analyse_transfer_time.py)

<img src="./plotting/images/compare_machines_imagesize_transfertime_imagej.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_transfertime_clij.png" width="300">

These plots were done with the [plotCompareMachinesTransferImageSize.py](https://github.com/clij/clij-benchmarking/tree/master/plotting/python/plotCompareMachinesTransferImageSize.py) script.

### Processing time over image size
We chose six operations to plot processing time over image size in detail. 

<img src="./plotting/images/compare_machines_imagesize_processing_time_AddImagesWeighted3D.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_processing_time_GaussianBlur3D.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_processing_time_MaximumZProjection.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_processing_time_Minimum3D.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_processing_time_RadialReslice.png" width="300">
<img src="./plotting/images/compare_machines_imagesize_processing_time_Reslice3D.png" width="300">

These plots were done with the [plotCompareMachinesImageSize.py](https://github.com/clij/clij-benchmarking/tree/master/plotting/python/plotCompareMachinesImageSize.py) script.

Some of the operations show different behaviour with small images compared to large images. We assume that the CPU cache 
allows ImageJ operations running on the CPU to finish faster with small images compared to large images.

### Processing time over operation specific parameters
Some operations have parameters influencing processing time. We chose two representative examples: the Gaussian Blur 
filter whose processing time might depend on its sigma parameter and the Minimum filter whose processing time might 
depend on the entered radius parameter.

<img src="./plotting/images/compare_machines_kernelsize_processing_time_GaussianBlur3D.png" width="300">
<img src="./plotting/images/compare_machines_kernelsize_processing_time_Minimum3D.png" width="300">

These plots were done with the [plotCompareMachinesKernelSize.py](https://github.com/clij/clij-benchmarking/tree/master/plotting/python/plotCompareMachinesKernelSize.py) script.

The Gaussian Blur filter in ImageJ is optimized for speed. Apparently, it is an implementation which is
independent from kernel size (e.g. using a FFT transform). Thus with very large kernels, it can perform faster than 
the GPU version which does not have this optimization built in. The Minimum filter shows an apparent polynomial time 
consumption with increasing filter radius. We assume it is a single-threaded implementation.

### Speedup of operations
We also generated an overview of speedup factors for all tested operations. The speedup was calculated relative to the
ImageJ operation executed on the laptop CPU.

![Image](plotting/images/compare_machines_all_operations.png" width="300">

This plots was generated with the [plotCompareMachinesAllOperations.py](https://github.com/clij/clij-benchmarking/tree/master/plotting/python/plotCompareMachinesAllOperations.py) script.

This table shows some operations to perform slower (speedup < 1) on the workstation 
CPU in comparison to the notebook CPU. We see the reason in the single thread performance resulting from the clock rate of the CPU.
While the [Intel i7-8650U](https://ark.intel.com/content/www/us/en/ark/products/124968/intel-core-i7-8650u-processor-8m-cache-up-to-4-20-ghz.html) 
is a high end mobile CPU with up to 4.2 GHz clock rate, the [Intel Xeon Silver 4110](https://ark.intel.com/content/www/us/en/ark/products/123547/intel-xeon-silver-4110-processor-11m-cache-2-10-ghz.html)
has a maximum clock rate of 3 GHz. On the other hand, there are operations shown which run faster on the workstation CPU. 
We assume that the algorithms implemented exploit multi-threading for higher performance yield. 

[Back to CLIJ documentation](https://clij.github.io/)








