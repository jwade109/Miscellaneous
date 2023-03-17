from copy import copy
from dataclasses import dataclass
import sys


def vstr(vial):
    return "".join(vial)


def state_repr(vials):
    if not vials:
        return ""
    return " ".join([vstr(x) for x in vials])


def count_empty(vial):
    return len([x for x in vial if x == "-"])


def get_top_element(vial):
    for i, e in enumerate(vial):
        if e != "-":
            n = 0
            # found a color; count them
            for j in range(i, len(vial)):
                if vial[j] == e:
                    n += 1
                else:
                    break
            return e, n
    return "-", 0


# returns the modified vials if successful, two empty lists otherwise
def pour_into(src, dst):

    src_empty = count_empty(src)
    dst_empty = count_empty(dst)

    # can't pour into a full vial
    if dst_empty == 0:
        return [], []

    # can't pour from an empty vial
    if src_empty == len(src):
        return [], []

    dst_top, n = get_top_element(dst)
    src_top, m = get_top_element(src)
    if src_top != dst_top and dst_top != "-":
        return [], []

    num_pour = min(m, dst_empty)

    new_src = copy(src)
    new_dst = copy(dst)

    num_removed = 0
    for i in range(len(new_src)):
        if new_src[i] == src_top:
            new_src[i] = "-"
            num_removed += 1
            if num_removed == num_pour:
                break
    num_added = 0
    for ir in range(len(new_dst)):
        i = len(new_dst) - ir - 1
        if new_dst[i] == "-":
            new_dst[i] = src_top
            num_added += 1
            if num_added == num_pour:
                break

    return new_src, new_dst


# generate all legal moves given a game state
def get_all_adjacent_game_states(vials):
    options = []
    for i in range(len(vials)):
        vi = vials[i]
        for j in range(len(vials)):
            if i == j:
                continue
            vj = vials[j]
            x, y = pour_into(vi, vj)
            if x and y:
                options.append((i, j, x, y))

    generated_states = set()
    generated_states.add(str(sorted(copy(vials))))
    ret = []
    for i, j, x, y in options:
        new_vials = copy(vials)
        new_vials[i] = x
        new_vials[j] = y
        key = str(sorted(copy(new_vials)))
        if key in generated_states:
            continue
        generated_states.add(key)
        ret.append((i, j, new_vials))
    return ret


@dataclass(frozen=True)
class StateNode:
    level: int = 0
    moves: list = None
    state: list = None
    lineage: list = None


def is_a_terminating_state(vials):
    states = get_all_adjacent_game_states(vials)
    return len(states) == 0


def is_win_condition(vials):
    return len([x for x in vials if len(set(y for y in x)) == 1]) == len(vials)


def search_all_child_states(root: StateNode, visited):

    rep = state_repr(sorted(root.state))
    key = str(sorted(root.state))
    if key in visited:
        stored = visited[key]
        if stored.level <= root.level:
            return []

    visited[key] = root

    ret = [root]
    children = get_all_adjacent_game_states(root.state)
    for i, (i, j, state) in enumerate(children):
        moves = copy(root.moves)
        moves.append((i, j))
        node = StateNode(root.level + 1, moves, state, root.lineage + f".{i}")
        results = search_all_child_states(node, visited)
        ret.extend(results)
    return ret


def load_vials_from_file(filename):
    f = open(filename, "r")
    lines = [x.strip() for x in f.readlines()]
    num_vials = None
    vials = []
    for x in lines:
        cells = x.split(" ")
        if num_vials is None:
            num_vials = len(cells)
            for i in range(len(cells)):
                vials.append([])
        elif num_vials != len(cells):
            print(f"Bad line: {x}")
            return []
        for i in range(len(cells)):
            vials[i].append(cells[i])
    return vials


def main():

    if len(sys.argv) < 2:
        print("Requires an input text file.")
        return 1

    filename = sys.argv[1]
    print(f"Loading {filename}.")

    vials = load_vials_from_file(filename)
    root = StateNode(0, [], vials, "r")

    all_states = {}
    search_all_child_states(root, all_states)

    for node in sorted(all_states.values(), key=lambda x: x.level):
        is_terminating = is_a_terminating_state(node.state)
        is_win = is_win_condition(node.state)
        if True or node.level == 0:
            print(f"{node.level}{'*' if is_terminating else ' '}" \
                f"{'$' if is_win else ' '}\t" \
                f"{state_repr(node.state)} " \
                f"{', '.join(str((x[0] + 1, x[1] + 1)) for x in node.moves)}")



if __name__ == "__main__":
    main()