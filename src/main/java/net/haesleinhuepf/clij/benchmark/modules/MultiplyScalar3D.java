package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class MultiplyScalar3D extends DefaultBenchmarkableModule {

    private Double scalar;

    public MultiplyScalar3D(double scalar) {
        this.scalar = scalar;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp3D, "Multiply...", "value=" + scalar + " stack");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().multiplyImageAndScalar(clb3D[0], clb3D[1], scalar.floatValue());
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + scalar;
    }
}
