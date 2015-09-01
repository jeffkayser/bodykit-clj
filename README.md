# bodykit-clj

A Clojure library for the Body Labs [BodyKit Instant API](http://developer.bodylabs.com/).

## Install

Add to `:dependencies` in Leiningen or Boot:

```clojure
[jeffkayser/bodykit-clj "0.1.0"]
```

## Use

### Get API credentials

You'll need an API access key and secret from Body Labs. As of late 2015 it's still in private beta, so you'll have to [request an invite](http://www.bodylabs.com/bodykit.html).

### REPL

```clojure
(require '[bodykit-clj.core :as bodykit])
```

### From application

```clojure
(ns my-app.core
  (:require [bodykit-clj.core :as bodykit]))
```

### Make API calls

bodykit-clj supports the [published API calls](http://developer.bodylabs.com/instant_api_reference.html). Pass in a map of parameters to override the defaults.

The methods share [common parameters](http://developer.bodylabs.com/measurement_guide.html#Standard-Scheme), so you may want to store them for reuse. Here we define two sets of measurements.

```clojure
(def foo {:gender       "female"
          :scheme       "standard"
          :size         "US 10"
          :unitSystem   "unitedStates"
          :measurements {:height              63
                         :weight              140
                         :bust_girth          36
                         :waist_girth         24
                         :low_hip_girth       36
                         :shirt_sleeve_length 28
                         :inseam              29
                         :mid_neck_girth      12}})

(def bar {:gender       "female"
          :scheme       "standard"
          :size         "US 6"
          :unitSystem   "unitedStates"
          :measurements {:height              63
                         :weight              120
                         :bust_girth          32
                         :waist_girth         22
                         :low_hip_girth       33
                         :shirt_sleeve_length 25
                         :inseam              27}})
```

Wrap calls with `with-auth` to provide an API access key and secret. Currently only [server-to-server](http://developer.bodylabs.com/authentication.html#Servertoserver-applications) authentication is supported, but this should work for most use cases.

#### Measurements

Get a map of several predicted measurements from a smaller number of measurements.

```clojure
(bodykit/with-auth :server my-access-key my-secret
  (bodykit/measurements foo))
```
      
***Output***

```clojure
{:output
 {:measurements
  {:side_neck_to_bust_point         10.27
   :bust_point_to_bust_point_width  8.48
   :outseam                         39.12
   :above_bust_girth                35.53
   :across_back_shoulder_width      14.81
   :neck_base_girth                 15.04
   :back_neck_to_mid_neck           1.24
   :thigh_girth                     22.55
   :knee_girth                      14.46
   :across_back_width               13.76
   :high_hip_height                 34.96
   :back_neck_height                53.88
   :waist_girth                     24
   :under_bust_height               42.99
   :calf_girth                      14.31
   :front_neck_to_mid_neck          1.75
   :ankle_height                    2.85
   :under_bust_girth                29.08
   :across_front_width              13.03
   :side_arm_length                 20.58
   :waist_height                    39.68
   :total_rise_length               26.18
   :waist_to_high_hip               3.79
   :mid_neck_girth                  12.21
   :bicep_girth                     10.82
   :armscye_girth                   15.85
   :wrist_girth                     5.99
   :shoulder_length                 4.22
   :forearm_girth                   9.08
   :thigh_height                    27.39
   :weight                          135
   :front_neck_height               51.94
   :front_neck_to_waist             13.98
   :bust_point_to_bust_point_halter 26.29
   :vertical_trunk_girth            59.08
   :front_neck_to_above_bust        3.5
   :waist_to_low_hip                7.77
   :back_neck_to_waist              16.09
   :back_neck_to_across_back        4.09
   :bust_girth                      36
   :elbow_girth                     9.19
   :bust_height                     45.67
   :knee_height                     17.01
   :front_neck_to_across_front      2.52
   :full_sleeve_length              27.28
   :low_hip_height                  31.67
   :calf_height                     11.66
   :low_hip_girth                   36
   :shirt_sleeve_length             29.18
   :high_hip_girth                  32.48
   :shoulder_drop                   1.79
   :ankle_girth                     9.37
   :inseam                          28.75
   :height                          63
   :back_arm_length                 21.39}}}
```

#### Mesh

Get a 3D mesh in [Wavefront OBJ](http://people.cs.clemson.edu/~dhouse/courses/405/docs/brief-obj-file-format.html) format.

```clojure
(bodykit/with-auth :server my-access-key my-secret
  (bodykit/mesh foo))
```

***Output***

![mesh output](https://github.com/jeffkayser/bodykit-clj/blob/master/resources/example.mesh-foo.stl)

[View interactive model](https://github.com/jeffkayser/bodykit-clj/blob/master/resources/example.mesh-foo.stl)

#### Mesh metrics

Get a map of physical metrics (e.g., volume, surface area) about the mesh predicted from the given measurements.

```clojure
(bodykit/with-auth :server my-access-key my-secret
  (bodykit/mesh-metrics foo))
```

***Output***

```clojure
{:output
 {:mesh_metrics
  {:total
   {:volume       56253
    :surface_area 15669}}}}
```

#### Heatmap

Get a 3D heat map comparison mesh, showing the difference of the two input meshes. The mesh is again in Wavefront OBJ format, but contains [color information](https://en.wikipedia.org/wiki/Wavefront_.obj_file#Geometric_Vertex). As vertex distance changes from zero to `:scale` centimeters, the mesh color changes from blue to red. If unspecified, `:scale` seems to default to 3 cm.

The first mesh specified in the `:bodies` parameter becomes the "representative" mesh onto which the heatmap is projected.

```clojure
(bodykit/with-auth :server my-access-key my-secret
  (bodykit/heatmap {:bodies [foo bar]))
```

***Output***

See `example.heatmap-foo+bar.*` in the [resources directory](./resources/).

## Viewing Wavefront OBJ files

Wavefront OBJ files are well-supported. Here are some free programs that can open them on Linux, OS X, and Windows:

- [MeshLab](http://meshlab.sourceforge.net/)
- [Autodesk Meshmixer](http://meshmixer.com/)

Note that only MeshLab can properly display colored meshes (i.e., from the `heatmap` method).

Simply choose the respective program's **Import** function to load and view the meshes.

## License

Copyright © 2015 [Jeff Kayser](https://jeffkayser.com/)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## TODO

- Support the [web application](http://developer.bodylabs.com/authentication.html#Web-applications) authentication scheme.
- Add tests

## Contribute

Pull requests are welcome.
