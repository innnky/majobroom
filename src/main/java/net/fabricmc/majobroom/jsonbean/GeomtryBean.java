package net.fabricmc.majobroom.jsonbean;

import java.util.List;

public class GeomtryBean {
    private int texturewidth;
    private int textureheight;
    private float visible_bounds_width;
    private float visible_bounds_height;
    private List<Float> visible_bounds_offset;
    private List<GeomtryBean.BonesBean> bones;

    public GeomtryBean() {
    }

    public int getTexturewidth() {
        return this.texturewidth;
    }

    public void setTexturewidth(int texturewidth) {
        this.texturewidth = texturewidth;
    }

    public int getTextureheight() {
        return this.textureheight;
    }

    public void setTextureheight(int textureheight) {
        this.textureheight = textureheight;
    }

    public float getVisible_bounds_width() {
        return this.visible_bounds_width;
    }

    public void setVisible_bounds_width(float visible_bounds_width) {
        this.visible_bounds_width = visible_bounds_width;
    }

    public float getVisible_bounds_height() {
        return this.visible_bounds_height;
    }

    public void setVisible_bounds_height(float visible_bounds_height) {
        this.visible_bounds_height = visible_bounds_height;
    }

    public List<Float> getVisible_bounds_offset() {
        return this.visible_bounds_offset;
    }

    public void setVisible_bounds_offset(List<Float> visible_bounds_offset) {
        this.visible_bounds_offset = visible_bounds_offset;
    }

    public List<GeomtryBean.BonesBean> getBones() {
        return this.bones;
    }

    public void setBones(List<GeomtryBean.BonesBean> bones) {
        this.bones = bones;
    }

    public static class BonesBean {
        private String name;
        private String parent;
        private List<Float> pivot;
        private List<Float> rotation;
        private List<GeomtryBean.BonesBean.CubesBean> cubes;

        public BonesBean() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent() {
            return this.parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public List<Float> getPivot() {
            return this.pivot;
        }

        public void setPivot(List<Float> pivot) {
            this.pivot = pivot;
        }

        public List<Float> getRotation() {
            return this.rotation;
        }

        public void setRotation(List<Float> rotation) {
            this.rotation = rotation;
        }

        public List<GeomtryBean.BonesBean.CubesBean> getCubes() {
            return this.cubes;
        }

        public void setCubes(List<GeomtryBean.BonesBean.CubesBean> cubes) {
            this.cubes = cubes;
        }

        public static class CubesBean {
            private List<Float> origin;
            private List<Float> size;
            private List<Integer> uv;
            private float inflate;

            public CubesBean() {
            }

            public float getInflate() {
                return this.inflate;
            }

            public void setInflate(float inflate) {
                this.inflate = inflate;
            }

            public List<Float> getOrigin() {
                return this.origin;
            }

            public void setOrigin(List<Float> origin) {
                this.origin = origin;
            }

            public List<Float> getSize() {
                return this.size;
            }

            public void setSize(List<Float> size) {
                this.size = size;
            }

            public List<Integer> getUv() {
                return this.uv;
            }

            public void setUv(List<Integer> uv) {
                this.uv = uv;
            }
        }
    }
}
